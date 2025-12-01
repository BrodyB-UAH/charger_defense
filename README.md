# ChargerDefense

## Instructions

- In NetBeans, open the `charger_defense` project
  - There might be a prompt about Project Problems; click `Resolve Problems...` -> `Resolve...` (NetBeans says that Gradle scripts need to be executed for project setup)
- Open the `charger_defense:lwjgl3` project by going to `charger_defense` -> `Sub Projects` -> `lwjgl3` from the NetBeans project explorer view
  - Right click on `lwjgl3` and select Open Project
- Right click the `charger_defense:lwjgl3` project and select `Run`
- If it doesn't work, the program can be run through `.\gradlew.bat lwjgl3:run` or `./gradlew lwjgl3:run`

### Usage

- The Play button will initially be disabled because a profile must be selected
- Create a profile from the Profile Selection view, select it, and go back to the main menu
- Press the Play button, select a map, and press play
- Click the arrow button on the right and purchase the unit by clicking Buy and clicking on a valid spot on the map near the path (when the indicator is green)
- Click Start Round in the top right

### Distribution

- Run `.\gradlew.bat lwjgl3:dist`
- The program will be bundled into `.\lwjgl3\build\libs\*.jar`

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
