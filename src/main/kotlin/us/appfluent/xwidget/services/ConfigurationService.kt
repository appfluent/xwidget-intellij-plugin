package us.appfluent.xwidget.services

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.components.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import us.appfluent.xwidget.*
import us.appfluent.xwidget.XWidgetConstants.Companion.DEFAULT_CONFIG_PATH
import us.appfluent.xwidget.utils.*
import java.io.File
import kotlin.io.path.pathString

class ConfigurationState: BaseState() {
    var autoGenerateEnabled by property(false)
}

@Service(Service.Level.PROJECT)
@State(name = "ConfigurationServiceState", storages = [Storage("FlutterXWidgetPlugin.xml")])
class ConfigurationService(val project: Project) : SimplePersistentStateComponent<ConfigurationState>(ConfigurationState()) {
    companion object {
        private val LOG: Logger = Logger.getInstance(ConfigurationService::class.java)
    }

    val configPath = toAbsolutePath(project, DEFAULT_CONFIG_PATH).pathString

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

    private val mutableIconSpecs: MutableMap<String, IconSpec>
        get() = iconSpecs as MutableMap

    private val mutableInflaterSpecs: MutableMap<String, InflaterSpec>
        get() = inflaterSpecs as MutableMap

    init {
        val fileService = project.getService(FileWatcherService::class.java)
        fileService.startWatching(configPath, ::onConfigFileChange)
    }

    override fun loadState(state: ConfigurationState) {
        super.loadState(state)
        toggleAutoGenerate(config, state.autoGenerateEnabled)
    }

    private fun toggleAutoGenerate(config: XWidgetConfig, enabled: Boolean) {
        val fileWatcherService = project.getService(FileWatcherService::class.java)
        for (inflaterSource in config.inflaters.sources) {
            if (enabled) fileWatcherService.startWatching(inflaterSource, ::onInflaterSpecFileChange)
            else fileWatcherService.stopWatching(inflaterSource)
        }
        for (iconSource in config.icons.sources) {
            if (enabled) fileWatcherService.startWatching(iconSource, ::onIconSpecFileChange)
            else fileWatcherService.stopWatching(iconSource)
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
            val path = toAbsolutePath(project, iconSource)
            val virtualFile = VirtualFileManager.getInstance().findFileByNioPath(path)
            if (virtualFile != null) {
                specs[virtualFile.path] = XWidgetUtils.getIconSpec(project, virtualFile)
            }
        }
        return specs
    }

    private fun readInflaterSpecFiles(): Map<String, InflaterSpec> {
        val specs: MutableMap<String, InflaterSpec> = mutableMapOf()
        for (inflaterSource in config.inflaters.sources) {
            val path = toAbsolutePath(project, inflaterSource)
            val virtualFile = VirtualFileManager.getInstance().findFileByNioPath(path)
            if (virtualFile != null) {
                specs[virtualFile.path] = XWidgetUtils.getInflaterSpec(project, virtualFile)
            }
        }
        return specs
    }

    private fun onConfigFileChange(file: VirtualFile) {
        LOG.debug("CHANGED: Config")
        val oldConfig = config
        config = readConfigFile()

        if (oldConfig.inflaters.sources.isDifferent(config.inflaters.sources) ||
            oldConfig.icons.sources.isDifferent(config.icons.sources)) {
            toggleAutoGenerate(oldConfig, false)
            toggleAutoGenerate(config, true)
            iconSpecs = readIconSpecFiles()
            inflaterSpecs = readInflaterSpecFiles()
            showNotification(project, "Generating components...")
            ActionManager
                .getInstance()
                .getAction("us.appfluent.xwidget.actions.generate-all")
                ?.actionPerformed(buildActionEvent(PluginActionPlaces.BACKGROUND))
        }
    }

    private fun onInflaterSpecFileChange(virtualFile: VirtualFile) {
        LOG.debug("CHANGED: Inflater Spec")
        val oldInflaterSpec = inflaterSpecs[virtualFile.path]
        val newInflaterSpec = XWidgetUtils.getInflaterSpec(project, virtualFile)
        if (oldInflaterSpec == null || oldInflaterSpec.isDifferent(newInflaterSpec)) {
            mutableInflaterSpecs[virtualFile.path] = newInflaterSpec
            showNotification(project, "Generating inflaters...")
            ActionManager
                .getInstance()
                .getAction("us.appfluent.xwidget.actions.generate-inflaters")
                ?.actionPerformed(buildActionEvent(PluginActionPlaces.BACKGROUND))
        }
    }

    private fun onIconSpecFileChange(virtualFile: VirtualFile) {
        LOG.debug("CHANGED: Icon Spec")
        val oldIconSpec = iconSpecs[virtualFile.path]
        val newIconSpec = XWidgetUtils.getIconSpec(project, virtualFile)
        if (oldIconSpec == null || oldIconSpec.isDifferent(newIconSpec)) {
            mutableIconSpecs[virtualFile.path] = newIconSpec
            showNotification(project, "Generating icons...")
            ActionManager
                .getInstance()
                .getAction("us.appfluent.xwidget.actions.generate-icons")
                ?.actionPerformed(buildActionEvent(PluginActionPlaces.BACKGROUND))
        }
    }

    private fun buildActionEvent(place: String): AnActionEvent {
        val dataContext = SimpleDataContext.getSimpleContext(CommonDataKeys.PROJECT, project)
        return AnActionEvent(
            null,
            dataContext,
            place,
            Presentation(),
            ActionManager.getInstance(),
            0
        )
    }
}

@Suppress("UNUSED")
class XWidgetConfig @JsonCreator constructor(
    @JsonProperty("inflaters") inflaters: XWidgetInflaters?,
    @JsonProperty("schema") schema: XWidgetSchema?,
    @JsonProperty("icons") icons: XWidgetIcons?,
    @JsonProperty("controllers") controllers: XWidgetControllers?
) {
    val inflaters: XWidgetInflaters = inflaters ?: XWidgetInflaters()
    val schema: XWidgetSchema = schema ?: XWidgetSchema()
    val icons: XWidgetIcons = icons ?: XWidgetIcons()
    val controllers: XWidgetControllers = controllers ?: XWidgetControllers()

    constructor() : this(null, null, null, null)
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
        return imports.isDifferent(other.imports) ||
                inflaters.isDifferent(other.inflaters)
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
        return imports.isDifferent(other.imports) ||
                icons.isDifferent(other.icons) ||
                iconSet.isDifferent(other.iconSet)
    }
}