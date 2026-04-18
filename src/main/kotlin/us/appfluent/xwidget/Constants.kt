package us.appfluent.xwidget

import us.appfluent.xwidget.utils.Version

class DartConstants {
    companion object {
        const val PUBSPEC_PATH = "pubspec.yaml"
        const val PUBSPEC_LOCK_PATH = "pubspec.lock"
    }
}

class XWidgetConstants {
    companion object {
        const val NAMESPACE = "http://www.appfluent.us/xwidget"
        const val DEFAULT_CONFIG_PATH = "xwidget_config.yaml"
        const val XWIDGET_SRC_PATH = "package:xwidget/src/xwidget.dart"

        // hot reload
        val HOT_RELOAD_SINCE_VERSION = Version.parse("0.4.2")

        // xwidget < 0.1.0
        val LEGACY_LAST_VERSION = Version.parse("0.0.52")
        const val LEGACY_CMD_GENERATE_ALL = "dart run xwidget:generate"
        const val LEGACY_CMD_GENERATE_ICONS = "dart run xwidget:generate --only icons"
        const val LEGACY_CMD_GENERATE_INFLATERS = "dart run xwidget:generate --only inflaters"
        const val LEGACY_CMD_GENERATE_CONTROLLERS = "dart run xwidget:generate --only controllers"
        const val LEGACY_CMD_INIT_NEW_APP = "dart run xwidget:init --new-app"
        const val LEGACY_CMD_INIT_EXISTING_APP = "dart run xwidget:init"

        const val CMD_GENERATE_ALL = "dart run xwidget_builder:generate"
        const val CMD_GENERATE_ICONS = "dart run xwidget_builder:generate --only icons"
        const val CMD_GENERATE_INFLATERS = "dart run xwidget_builder:generate --only inflaters"
        const val CMD_GENERATE_CONTROLLERS = "dart run xwidget_builder:generate --only controllers"
        const val CMD_INIT_NEW_APP = "dart run xwidget_builder:init --new-app"
        const val CMD_INIT_EXISTING_APP = "dart run xwidget_builder:init"
        const val CMD_ADD_XWIDGET = "flutter pub add xwidget"
        const val CMD_ADD_XWIDGET_BUILDER = "flutter pub add dev:xwidget_builder"

        const val URL_DOCUMENTATION = "https://pub.dev/packages/xwidget"
        const val URL_ISSUES = "https://github.com/appfluent/xwidget/issues"

        const val ELEMENT_START_BREAKPOINT = "breakpoint(\"start\");"
        const val ELEMENT_END_BREAKPOINT = "breakpoint(\"end\");"
    }
}

class PluginActionPlaces {
    companion object {
        const val BACKGROUND = "background"
    }
}