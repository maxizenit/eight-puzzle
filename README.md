# eight-puzzle

**eight-puzzle** - это программа, которая ищет решение игры "Пятнашки" по указанным начальному и целевому состояниям.

## Алгоритм работы

Программа перебирает возможные ходы из стартового состояния, строя дерево состояний.
Поиск целевого состояния идёт в глубину, с ограничением глубины.
Исходя из текущего состояния, выбираются возможные направления ходов
(список возможных ходов) зависит от расположения пустой фишки на доске.

Возможные ходы упорядочиваются, исходя из того, сколько фишек не на своём месте будет находиться в новом состоянии и
какая сумма манхэттенских расстояний для всех фишек там выходит.

При переходе к состоянию оно проверяется на равенство целевому (в таком случае
печатается пройденный путь и число ходов для его достижения) либо на повтор
(состояния сохраняются в кэш, и если данное состояние достигалось ранее на меньшей глубине,
поиск дальше не пойдёт).

## Инструкция по работе с программой

После запуска программы необходимо ввести стартовое и конечные состояния,
максимальную глубину поиска и указать, необходимо ли запустить пошаговый режим.

Состояния указываются в виде числа, которое формируется по следующим правилам:

* отсутствие фишки считается как 0
* все цифры фишек записываются в число поочерёдно, построчно (например,
  доска, у которой 1 строка - 123, 2 строка - 456, 3 строка - 780 записывается как 123456780)
* если фишка отсутствует на самой первой клетке, 0 в начале числа можно не указывать

Для пошагового режима необходимо после каждого шага нажимать Enter для перехода к следующему.

## Сборка и запуск

Для сборки и запуска программы требуется JDK версии не ниже 17.

Сборка осуществляется командой `./gradlew jar` (выполнять в корне).

Для запуска собранного jar-файла необходимо перейти в папку `build/libs` и запустить программу
командой `java -jar eight-puzzle-heuristic.jar`. Для слишком больших глубин поиска необходимо указать --Xss
параметр:`java -jar -Xss256M eight-puzzle-heuristic.jar`.