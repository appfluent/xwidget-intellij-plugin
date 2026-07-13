### 0.2.0 (Jul 13, 2026)

* Added support for xwidget_builder 0.7.0 projects. `xwidget_config.yaml` is now discovered
  in the `.xwidget/` config directory with fallback to the project root, and both locations
  are watched — config reloads, hot reload paths, and auto-generation pick up a project's
  migration to the new layout without restarting the IDE.
* Fragment features (navigation, gutter actions) now recognize the new fragment namespace
  `https://xwidget.dev/fragments` alongside the legacy `http://www.appfluent.us/xwidget`,
  so both migrated and pre-0.7 projects work.
* EL syntax highlighting now activates only in XWidget fragment documents (root
  element in an XWidget fragment namespace). Other XML that happens to contain
  `${...}` — Maven poms, Spring configs — no longer gets EL colors or false
  "bad character" errors. Fragments without an `xmlns` declaration lose EL
  highlighting; add the namespace to restore it (it also enables validation).
* Compatible with IntelliJ IDEA 2025.3 through 2026.2 and Android Studio.
* Schema-driven completion and validation follow the relocated, renamed schemas
  (`.xwidget/fragments_schema.g.xsd` etc.) automatically via the IDE's namespace index —
  including the new routes and values schemas.

### 0.1.8 (Apr 23, 2026)

* Restored Android Studio 2025.3 compatibility. Hot reload now works across IntelliJ IDEA 2025.3 through 2026.1 and Android Studio 2025.3+.

### 0.1.7 (Apr, 17 2026)

* Added hot reload for XML fragments and values. Edit a fragment or value resource file in the IDE and see
  changes reflected instantly in the running app — no restart required. Works on physical devices, emulators,
  simulators, and desktop. Requires XWidget package v0.5.0 or later.
* Compatible with IntelliJ IDEA 2026.1 and Android Studio.

### 0.1.6 (Apr, 15 2026)

* Updated plugin compatibility to support IntelliJ IDEA 2025.3 through 2026.1 and Android Studio.

### 0.1.5 (Nov 22, 2025)

* Added support for XWidget versions >= 0.1.0

### 0.1.4 (Oct 21, 2024)

* Added `Auto Generate` toggle option the 'Tools' menu
* Fixed issue with project initialization actions

### 0.1.3 (Oct 8, 2024)

* Fixed Android Studio compatibility issues
* Minor bug fixes

### 0.1.2 (Oct 8, 2024)

* Added context aware popup menu items to navigate to controllers and fragments.
* Changed project id from `us.appfluent.xwidget-jetbrains-plugin` to `us.appfluent.xwidget-intellij-plugin`
* Removed uses of deprecated classes and methods

### 0.1.1 (Oct 5, 2024)

* Added 'Tools' menu items to initialize project, view documentation, and view issues.
* Updated Gradle Plugin from 1.x to 2.x

### 0.1.0 (Oct 4, 2024)

* Initial release.