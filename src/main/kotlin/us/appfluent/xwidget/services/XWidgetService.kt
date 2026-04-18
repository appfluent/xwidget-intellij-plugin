package us.appfluent.xwidget.services

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.JsonObject
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.components.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import org.dartlang.vm.service.VmService
import org.dartlang.vm.service.consumer.ServiceExtensionConsumer
import org.dartlang.vm.service.consumer.VMConsumer
import org.dartlang.vm.service.element.RPCError
import org.dartlang.vm.service.element.VM
import us.appfluent.xwidget.DartConstants.Companion.PUBSPEC_LOCK_PATH
import us.appfluent.xwidget.PluginActionPlaces
import us.appfluent.xwidget.XWidgetConstants.Companion.DEFAULT_CONFIG_PATH
import us.appfluent.xwidget.utils.DartUtils
import us.appfluent.xwidget.utils.FileUtils.Companion.findVirtualFile
import us.appfluent.xwidget.utils.FileUtils.Companion.toAbsolutePath
import us.appfluent.xwidget.utils.UiUtils.Companion.showNotification
import us.appfluent.xwidget.utils.Version
import us.appfluent.xwidget.utils.XWidgetUtils.Companion.getIconSpec
import us.appfluent.xwidget.utils.XWidgetUtils.Companion.getInflaterSpec
import us.appfluent.xwidget.utils.isDifferent2
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.pathString

class XWidgetState: BaseState() {
    var autoGenerateEnabled by property(false)
}

@Service(Service.Level.PROJECT)
@State(name = "ConfigurationServiceState", storages = [Storage("FlutterXWidgetPlugin.xml")])
class XWidgetService(val project: Project) : SimplePersistentStateComponent<XWidgetState>(XWidgetState()) {
    companion object {
        private val LOG: Logger = Logger.getInstance(XWidgetService::class.java)
    }

    val configPath = toAbsolutePath(project, DEFAULT_CONFIG_PATH).pathString
    val pubspecLockPath = toAbsolutePath(project, PUBSPEC_LOCK_PATH).pathString

    var pubspecLock = DartUtils.readPubspecLockFile(project)
        private set

    var config: XWidgetConfig = readConfigFile()
        private set

    var iconSpecs: Map<String, IconSpec> = readIconSpecFiles()
        private set

    var inflaterSpecs: Map<String, InflaterSpec> = readInflaterSpecFiles()
        private set

    var autoGenerateEnabled: Boolean
        get() = state.autoGenerateEnabled
        set(enabled) {
            state.autoGenerateEnabled = enabled
            toggleAutoGenerate(config, enabled)
        }

    val version: Version?
        get() = pubspecLock?.getPackageVersion("xwidget")

    val builderVersion: Version?
        get() = pubspecLock?.getPackageVersion("xwidget_builder")

    private val mutableIconSpecs: MutableMap<String, IconSpec>
        get() = iconSpecs as MutableMap

    private val mutableInflaterSpecs: MutableMap<String, InflaterSpec>
        get() = inflaterSpecs as MutableMap

    init {
        val fileService = project.getService(FileWatcherService::class.java)
        fileService.startWatching("reloadConfig", configPath, ::onConfigFileChange)
        fileService.startWatching("reloadPubspec", pubspecLockPath, ::onPubspecLockFileChange)
    }

    override fun loadState(state: XWidgetState) {
        super.loadState(state)
        toggleAutoGenerate(config, state.autoGenerateEnabled)
    }

    fun startHotReloadFragments(vmService: VmService, sessionId: String) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        val fragmentsPath = config.fragmentsPath + "/"
        val basePath = Paths.get("${project.basePath}/$fragmentsPath")
        fileWatcherService.startWatching("hotReloadFragments_$sessionId", fragmentsPath) { file ->
            if (file.name.endsWith(".xml")) {
                val fqn = basePath.relativize(Paths.get(file.path)).toString()
                hotReloadFragment(vmService, fqn, file)
            }
        }
    }

    fun stopHotReloadFragments(sessionId: String) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        fileWatcherService.stopWatching("hotReloadFragments_$sessionId")
    }

    fun startHotReloadValues(vmService: VmService, sessionId: String) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        val valuesPath = config.valuesPath + "/"
        fileWatcherService.startWatching("hotReloadValues_$sessionId", valuesPath) { file ->
            if (file.name.endsWith(".xml")) {
                hotReloadValues(vmService, file)
            }
        }
    }

    fun stopHotReloadValues(sessionId: String) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        fileWatcherService.stopWatching("hotReloadValues_$sessionId")
    }

    private fun hotReloadFragment(vmService: VmService, fqn: String, file: VirtualFile) {
        vmService.getVM(object : VMConsumer {
            override fun received(vm: VM) {
                val params = JsonObject().apply {
                    addProperty("fqn", fqn)
                    addProperty("content", String(file.contentsToByteArray()))
                }
                vmService.callServiceExtension(
                    vm.isolates.first().id,
                    "ext.xwidget.updateFragment",
                    params,
                    object : ServiceExtensionConsumer {
                        override fun received(response: JsonObject) {
                            LOG.debug("Fragment updated: $fqn")
                        }
                        override fun onError(error: RPCError) {
                            LOG.warn("Failed to update fragment: ${error.message}")
                        }
                    }
                )
            }
            override fun onError(error: RPCError) {
                LOG.warn("Failed to get VM: ${error.message}")
            }
        })
    }

    private fun hotReloadValues(vmService: VmService, file: VirtualFile) {
        vmService.getVM(object : VMConsumer {
            override fun received(vm: VM) {
                val params = JsonObject().apply {
                    addProperty("content", String(file.contentsToByteArray()))
                }
                vmService.callServiceExtension(
                    vm.isolates.first().id,
                    "ext.xwidget.updateValues",
                    params,
                    object : ServiceExtensionConsumer {
                        override fun received(response: JsonObject) {
                            LOG.debug("Values updated")
                        }
                        override fun onError(error: RPCError) {
                            LOG.warn("Failed to update values: ${error.message}")
                        }
                    }
                )
            }
            override fun onError(error: RPCError) {
                LOG.warn("Failed to get VM: ${error.message}")
            }
        })
    }

    private fun toggleAutoGenerate(config: XWidgetConfig, enabled: Boolean) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        for (inflaterSource in config.inflaters.sources) {
            val absolutePath = toAbsolutePath(project, inflaterSource).pathString
            val watcherId = "autoGenInflaters:$absolutePath"
            if (enabled) fileWatcherService.startWatching(watcherId, absolutePath, ::onInflaterSpecFileChange)
            else fileWatcherService.stopWatching(watcherId)
        }
        for (iconSource in config.icons.sources) {
            val absolutePath = toAbsolutePath(project, iconSource).pathString
            val watcherId = "autoGenIcons:$absolutePath"
            if (enabled) fileWatcherService.startWatching(watcherId, absolutePath, ::onIconSpecFileChange)
            else fileWatcherService.stopWatching(watcherId)
        }
    }

    private fun readConfigFile(): XWidgetConfig {
        try {
            val mapper = ObjectMapper(YAMLFactory())
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val configFile = File(configPath)
            if (configFile.exists()) {
                return mapper.readValue(configFile, XWidgetConfig::class.java)
            }
        } catch (e: Exception) {
            val message = "Problem reading config file."
            showNotification(project, message, NotificationType.ERROR)
            LOG.warn(message, e)
        }
        return XWidgetConfig()
    }

    private fun readIconSpecFiles(): Map<String, IconSpec> {
        val specs: MutableMap<String, IconSpec> = mutableMapOf()
        for (iconSource in config.icons.sources) {
            val virtualFile = findVirtualFile(project, iconSource)
            if (virtualFile != null) {
                specs[virtualFile.path] = getIconSpec(project, virtualFile)
            }
        }
        return specs
    }

    private fun readInflaterSpecFiles(): Map<String, InflaterSpec> {
        val specs: MutableMap<String, InflaterSpec> = mutableMapOf()
        for (inflaterSource in config.inflaters.sources) {
            val virtualFile = findVirtualFile(project, inflaterSource)
            if (virtualFile != null) {
                specs[virtualFile.path] = getInflaterSpec(project, virtualFile)
            }
        }
        return specs
    }

    private fun onConfigFileChange(file: VirtualFile) {
        LOG.debug("CHANGED: Config")
        val oldConfig = config
        config = readConfigFile()
        if (oldConfig.inflaters.sources.isDifferent2(config.inflaters.sources) ||
            oldConfig.icons.sources.isDifferent2(config.icons.sources)) {
            toggleAutoGenerate(oldConfig, false)
            toggleAutoGenerate(config, true)
            iconSpecs = readIconSpecFiles()
            inflaterSpecs = readInflaterSpecFiles()
            showNotification(project, "Generating components...")
            ActionManager.getInstance().tryToExecute(
                ActionManager.getInstance().getAction("us.appfluent.xwidget.actions.generate-all"),
                null,
                null,
                PluginActionPlaces.BACKGROUND,
                false
            )
        }
    }

    private fun onInflaterSpecFileChange(file: VirtualFile) {
        LOG.debug("CHANGED: Inflater Spec")
        val oldInflaterSpec = inflaterSpecs[file.path]
        val newInflaterSpec = getInflaterSpec(project, file)
        if (oldInflaterSpec == null || oldInflaterSpec.isDifferent(newInflaterSpec)) {
            mutableInflaterSpecs[file.path] = newInflaterSpec
            showNotification(project, "Generating inflaters...")
            ActionManager.getInstance().tryToExecute(
                ActionManager.getInstance().getAction("us.appfluent.xwidget.actions.generate-inflaters"),
                null,
                null,
                PluginActionPlaces.BACKGROUND,
                false
            )
        }
    }

    private fun onIconSpecFileChange(file: VirtualFile) {
        LOG.debug("CHANGED: Icon Spec")
        val oldIconSpec = iconSpecs[file.path]
        val newIconSpec = getIconSpec(project, file)
        if (oldIconSpec == null || oldIconSpec.isDifferent(newIconSpec)) {
            mutableIconSpecs[file.path] = newIconSpec
            showNotification(project, "Generating icons...")
            ActionManager.getInstance().tryToExecute(
                ActionManager.getInstance().getAction("us.appfluent.xwidget.actions.generate-icons"),
                null,
                null,
                PluginActionPlaces.BACKGROUND,
                false
            )
        }
    }

    private fun onPubspecLockFileChange(file: VirtualFile) {
        LOG.debug("CHANGED: Pubspec Lock File")
        pubspecLock = DartUtils.readPubspecLockFile(project)
    }
}

@Suppress("UNUSED")
class XWidgetConfig @JsonCreator constructor(
    @JsonProperty("fragmentsPath") fragmentsPath: String?,
    @JsonProperty("valuesPath") valuesPath: String?,
    @JsonProperty("inflaters") inflaters: XWidgetInflaters?,
    @JsonProperty("schema") schema: XWidgetSchema?,
    @JsonProperty("icons") icons: XWidgetIcons?,
    @JsonProperty("controllers") controllers: XWidgetControllers?
) {
    val fragmentsPath: String = fragmentsPath ?: "resources/fragments"
    val valuesPath: String = valuesPath ?: "resources/values"
    val inflaters: XWidgetInflaters = inflaters ?: XWidgetInflaters()
    val schema: XWidgetSchema = schema ?: XWidgetSchema()
    val icons: XWidgetIcons = icons ?: XWidgetIcons()
    val controllers: XWidgetControllers = controllers ?: XWidgetControllers()

    constructor() : this(null, null, null, null, null, null)
}

@Suppress("UNUSED")
class XWidgetInflaters @JsonCreator constructor(
    @JsonProperty("target") target: String?,
    @JsonProperty("imports") imports: List<String>?,
    @JsonProperty("sources") sources: List<String>?,
    @JsonProperty("includes") includes: List<String>?,
    @JsonProperty("constructor_exclusions") constructorExclusions: List<String>?,
    @JsonProperty("constructor_arg_defaults") constructorArgDefaults: Map<String, String>?,
    @JsonProperty("constructor_arg_parsers") constructorArgParsers: Map<String, String>?,
) {
    val target: String = target ?: ""
    val imports: List<String> = imports ?: listOf()
    val sources: List<String> = sources ?: listOf("lib/xwidget/inflater_spec.dart")
    val includes: List<String> = includes ?: listOf()
    val constructorExclusions: List<String> = constructorExclusions ?: listOf()
    val constructorArgDefaults: Map<String, String> = constructorArgDefaults ?: mapOf()
    val constructorArgParsers: Map<String, String> = constructorArgParsers ?: mapOf()

    constructor() : this(null, null, null, null, null, null, null)
}

@Suppress("UNUSED")
class XWidgetSchema @JsonCreator constructor(
    @JsonProperty("target") target: String?,
    @JsonProperty("template") template: String?,
    @JsonProperty("types") types: Map<String, String>?,
    @JsonProperty("attribute_exclusions") attributeExclusions: List<String>?,
) {
    val target: String = target ?: ""
    val template: String = template ?: ""
    val types: Map<String, String> = types ?: mapOf()
    val attributeExclusions: List<String> = attributeExclusions ?: listOf()

    constructor() : this(null, null, null, null)
}

@Suppress("UNUSED")
class XWidgetIcons @JsonCreator constructor(
    @JsonProperty("target") target: String?,
    @JsonProperty("imports") imports: List<String>?,
    @JsonProperty("sources") sources: List<String>?,
) {
    val target: String = target ?: ""
    val imports: List<String> = imports ?: listOf()
    val sources: List<String> = sources ?: listOf("lib/xwidget/icon_spec.dart")

    constructor() : this(null, null, null)
}

@Suppress("UNUSED")
class XWidgetControllers @JsonCreator constructor(
    @JsonProperty("target") target: String?,
    @JsonProperty("imports") imports: List<String>?,
    @JsonProperty("sources") sources: List<String>?,
) {
    val target: String = target ?: ""
    val imports: List<String> = imports ?: listOf()
    val sources: List<String> = sources ?: listOf()

    constructor() : this(null, null, null)
}

@Suppress("UNUSED")
class InflaterSpec constructor(
    imports: List<PsiElement>?,
    inflaters: List<PsiElement>?,
) {
    val imports: List<PsiElement> = imports ?: listOf()
    val inflaters: List<PsiElement> = inflaters ?: listOf()

    constructor() : this(null, null)

    fun isDifferent(other: InflaterSpec): Boolean {
        return imports.isDifferent2(other.imports) ||
                inflaters.isDifferent2(other.inflaters)
    }
}

@Suppress("UNUSED")
class IconSpec constructor(
    imports: List<PsiElement>?,
    icons: List<PsiElement>?,
    iconSet: List<PsiElement>?,
) {
    val imports: List<PsiElement> = imports ?: listOf()
    val icons: List<PsiElement> = icons ?: listOf()
    val iconSet: List<PsiElement> = iconSet ?: listOf()

    constructor() : this(null, null, null)

    fun isDifferent(other: IconSpec): Boolean {
        return imports.isDifferent2(other.imports) ||
                icons.isDifferent2(other.icons) ||
                iconSet.isDifferent2(other.iconSet)
    }
}