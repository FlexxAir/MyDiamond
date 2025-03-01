  **ENG:**

## Description:

* MyDiamond is a powerful and user-friendly Minecraft (Spigot/Paper) plugin that allows server administrators to manage players' diamond balances. It supports SQLite storage, automatic reminders for available diamonds, and update checks from SpigotMC.

## Plugin Features:

✅ Give and take diamonds – Admins can add or remove diamonds from players using /mydiamond give/take

✅ Balance check – Players and admins can check the number of diamonds they have (/mydiamond see)

✅ Automatic reminders – Players will be notified if they have unclaimed diamonds

✅ SQLite storage – Reliable data storage, even after server restarts

✅ Multilingual support – Localization in multiple languages (default ru_RU)

✅ Automatic update checks – Notifies the admin in the console when a new version is available

## Commands & Permissions:

* /mydiamond give <player> <amount> Give diamonds to a player

* /mydiamond take <player> <amount> Remove diamonds from a player

* /mydiamond see <player> Check a player's diamond balance

* /mydiamond reload Reload the plugin configuration

* mydiamond.give

* mydiamond.reload

* mydiamond.see

* mydiamond.take

## Configuration (config.yml):

* language: ru_RU # Select language

* reminder-interval: 5 # Reminder interval (in minutes)

## How to Install?

1️⃣ Download MyDiamond.jar

2️⃣ Move the file to your server’s plugins folder

3️⃣ Restart the server

4️⃣ Configure config.yml and reload with /mydiamond reload

----------------------------------------------------------------------------------------

## Building
#### Requirements
* Java 21 JDK or newer
#### Compiling from source
```sh
git clone https://github.com/FlexxAir/MyDiamond.git
cd MyDiamond/
./gradlew.bat ShadowJar
```

You can find the output jars in the `loader/build/libs` or `build/libs` directories.
