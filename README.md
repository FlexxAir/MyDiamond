ENG:

Description:
MyDiamond is a powerful and user-friendly Minecraft (Spigot/Paper) plugin that allows server administrators to manage players' diamond balances. It supports SQLite storage, automatic reminders for available diamonds, and update checks from SpigotMC.

Plugin Features:
✅ Give and take diamonds – Admins can add or remove diamonds from players using /mydiamond give/take
✅ Balance check – Players and admins can check the number of diamonds they have (/mydiamond see)
✅ Automatic reminders – Players will be notified if they have unclaimed diamonds
✅ SQLite storage – Reliable data storage, even after server restarts
✅ Multilingual support – Localization in multiple languages (default ru_RU)
✅ Automatic update checks – Notifies the admin in the console when a new version is available

Commands & Permissions:
Command Description Permission
/mydiamond give <player> <amount> Give diamonds to a player
/mydiamond take <player> <amount> Remove diamonds from a player
/mydiamond see <player> Check a player's diamond balance
/mydiamond reload Reload the plugin configuration

mydiamond.give
mydiamond.reload
mydiamond.see
mydiamond.take
Configuration (config.yml):
language: ru_RU # Select language
reminder-interval: 5 # Reminder interval (in minutes)

How to Install?
1️⃣ Download MyDiamond.jar
2️⃣ Move the file to your server’s plugins folder
3️⃣ Restart the server
4️⃣ Configure config.yml and reload with /mydiamond reload

----------------------------------------------------------------------------------------

RUS:

MyDiamond – это мощный и удобный плагин для Minecraft (Bukkit/Paper), который позволяет администраторам сервера управлять балансом алмазов игроков. Он поддерживает хранение данных в SQLite, автоматические напоминания о доступных алмазах и проверку обновлений с SpigotMC.

Функции плагина:
✅ Выдача и снятие алмазов – администраторы могут добавлять или забирать алмазы у игроков с помощью команды /mydiamond give/take
✅ Просмотр баланса – игроки и администраторы могут проверять количество доступных алмазов (/mydiamond see)
✅ Напоминания – плагин автоматически уведомляет игроков, если у них есть алмазы, ожидающие получения
✅ Сохранение в SQLite – надежное хранение данных, даже после перезапуска сервера
✅ Многоязычная поддержка – локализация на разные языки (по умолчанию ru_RU)
✅ Автоматическая проверка обновлений – если выходит новая версия, администратор получит уведомление в консоли

Команды и права:
/mydiamond give <игрок> <количество> Выдать алмазы игроку
/mydiamond take <игрок> <количество> Забрать алмазы у игрока
/mydiamond see <игрок> Посмотреть баланс алмазов
/mydiamond reload Перезагрузить конфигурацию

mydiamond.give
mydiamond.reload
mydiamond.see
mydiamond.take
Конфигурация (config.yml):
language: ru_RU # Выбор языка
reminder-interval: 5 # Интервал напоминаний (в минутах)
Как установить плагин?
1️⃣ Скачайте MyDiamond.jar
2️⃣ Переместите файл в папку plugins на сервере
3️⃣ Перезапустите сервер
4️⃣ Настройте config.yml и перезапустите сервер командой /mydiamond reload