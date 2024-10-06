package us.appfluent.xwidget.actions


class GenerateAllAction: CommandAction("dart run xwidget:generate")
class GenerateInflatersAction: CommandAction("dart run xwidget:generate --only inflaters")
class GenerateIconsAction: CommandAction("dart run xwidget:generate --only icons")
class GenerateControllersAction: CommandAction("dart run xwidget:generate --only controllers")

class InitNewProjectAction: CommandAction("dart run xwidget:init --new-app")
class InitExistingProjectAction: CommandAction("dart run xwidget:init")

class ViewDocumentationAction : WebsiteAction("https://pub.dev/packages/xwidget")
class ViewIssuesAction : WebsiteAction("https://github.com/appfluent/xwidget/issues")