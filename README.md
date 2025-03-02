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

* /mydiamond collect OR /mydiamond - Give diamonds to inventory

* /mydiamond give <player> <amount> Give diamonds to a player balance

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
* Git
#### Compiling from source
```sh
git clone https://github.com/FlexxAir/MyDiamond.git
cd MyDiamond/
./gradlew.bat ShadowJar
```

You can find the output jars in the `loader/build/libs` or `build/libs` directories.

## License
[`LICENSE`](https://github.com/FlexxAir/MyDiamond/blob/main/LICENSE)
MIT License

Copyright (c) 2025 Filatov Sergei <filwork@lsb.ink>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
