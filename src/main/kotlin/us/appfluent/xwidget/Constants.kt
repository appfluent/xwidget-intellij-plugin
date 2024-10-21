package us.appfluent.xwidget

class XWidgetConstants {
    companion object {
        const val NAMESPACE = "http://www.appfluent.us/xwidget"
        const val DEFAULT_CONFIG_PATH = "xwidget_config.yaml"

        const val CMD_GENERATE_ALL = "dart run xwidget:generate"
        const val CMD_GENERATE_ICONS = "dart run xwidget:generate --only icons"
        const val CMD_GENERATE_INFLATERS = "dart run xwidget:generate --only inflaters"
        const val CMD_GENERATE_CONTROLLERS = "dart run xwidget:generate --only controllers"
        const val CMD_INIT_NEW_APP = "dart run xwidget:init --new-app"
        const val CMD_INIT_EXISTING_APP = "dart run xwidget:init"
        const val CMD_ADD_XWIDGET = "flutter pub add xwidget"

        const val URL_DOCUMENTATION = "https://pub.dev/packages/xwidget"
        const val URL_ISSUES = "https://github.com/appfluent/xwidget/issues"
    }
}

class PluginActionPlaces {
    companion object {
        const val BACKGROUND = "background"
    }
}