# Работа с файлами

Задача
Цель задания

Поработать с файлами с использованием assets, internal storage и external storage.
Поработать с SharedPreferences.
 

Что нужно сделать

1. Создайте фрагмент, в котором содержится EditText и Button.

2. Когда пользователь вводит в EditText ссылку для скачивания файла и нажимает кнопку, файл должен скачиваться и сохраняться в папку external storage/files/<ваше название>.

3. Имя файла должно состоять из имени по ссылке и timestamp. 

Пример: https://gitlab.skillbox.ru/learning_materials/android_basic/-/raw/master/README.md

Название файла:

1601841925_README.md

4. После успешного сохранения файла сохраняйте информацию о нём в SharedPreferences. 

Ключ — url (https://gitlab.skillbox.ru/learning_materials/android_basic/-/raw/master/README.md).

Значение — название файла (1601841925_README.md).

5. При неудачном скачивании файла, если возникла какая-то ошибка, удаляйте файл.

6. При скачивании файла отображайте loader и блокируйте EditText, Button.

7. По окончании загрузки файла выводите toast с текстом «Файл README.md успешно загружен». 

8. При вводе url, по которому уже был скачан файл, выводите toast, что файл уже был скачан. Для этого перед загрузкой файла проверяйте, есть ли запись в SharedPreferences.

9. Добавьте текстовый файл в assets. В текстовом файле должны быть ссылки, файлы по которым скачиваются при первом запуске приложения. 

10. Для того, чтобы проверить, является ли запуск первым, используйте флаг и храните его в SharedPreferences.

11. По желанию используйте DataStore вместо SharedPreferences.

 

Критерии оценки

Код оформлен в соответствии с правилами.
Соблюдён принцип инкапсуляции с помощью модификаторов доступа.
Классы являются не финальными (open, abstract) только при необходимости.
Текстовые строки не являются захардкоженными и используются из ресурсов.
Код разнесён по сущностям в соответствии с их ответственностями.
Работа с файлами и сетью происходит в фоновом потоке.
Обрабатываются возникающие ошибки, проверяется external storage state.
Файловые потоки корректно закрываются.
При длительных операциях и ошибках происходит оповещение пользователя.
Выполнены все пункты в соответствии с заданием.