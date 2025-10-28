# ChargerDefense

## Instructions

- In NetBeans, open the `charger_defense` project
- Open the `charger_defense:lwjgl3` project by going to `charger_defense` -> `Sub Projects` -> `lwjgl3` from the NetBeans project explorer view
  - Right click on `lwjgl3` and select Open Project
- Right click the `charger_defense:lwjgl3` project and select "Run"
- If it doesn't work, the program can be run through `.\gradlew.bat lwjgl3:run` or `./gradlew lwjgl3:run`

### Usage

- The Play button will initially be disabled because a profile must be selected
- Create a profile from the Profile Selection view, select it, and go back to the main menu
- Press the Play button, select a map, and press play

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
