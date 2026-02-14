package lesson04.Homework04

import jdk.jfr.Enabled
import kotlin.random.Random

//Перегрузка операторов

// 1. Доступ к элементу по индексу ([ ]) и проверка наличия через in
//Есть класс Inventory, внутри которого хранится список строк items.

class Inventory {
    private val items = mutableListOf<String>()

//Перегрузи оператор + чтобы добавлять новые элементы в список.
    operator fun plus(element: String): Inventory {
        items.add(element)
        return this
    }

//Перегрузи оператор [ ], чтобы получать предмет по индексу.
    operator fun get(index: Int): String {
    val item = items[index]
    return item
}
//Перегрузи оператор in, чтобы проверять вхождение строки в список items.
    operator fun contains(element: String): Boolean {
    items.contains(element)
    return element in items
    }
}

//2. Инверсия состояния (!)
//Есть класс Toggle с полем enabled: Boolean.
//Перегрузи оператор !, чтобы он возвращал новый объект с противоположным состоянием.

class Toggle (val enabled: Boolean){
    operator fun not(): Toggle{
        return Toggle(!enabled)
    }
}

//3. Умножение значения (*)
//Есть класс Price с полем amount: Int.
//Перегрузи оператор *, чтобы можно было умножать цену на целое число (например, количество товаров).

class Price (val amount: Int){

    operator fun times(multiply: Int): Price {
        val result = Price(amount * multiply)
        return result
    }
}

//4. Диапазон значений (..)
//Есть класс Step с полем number: Int.
//Перегрузи оператор .., чтобы можно было создавать диапазон шагов между двумя объектами Step.
//Сделай возможной проверку: входит ли один Step в диапазон шагов с помощью оператора in.
// Обрати внимание, что это обратная операция и нужно расширять класс IntRange для проверки вхождения в него Step.

    //пока не делала

//5. Последовательное объединение (+)
//Есть класс Log с полем entries: List<String>.
//Перегрузи оператор +, чтобы при сложении логов записи объединялись в один лог.

class Log (val entries: List<String>) {
    operator fun plus(other: Log): Log {
        val newList = entries + other.entries
        return Log(newList)
    }
}

// Инфиксные функции

//Генератор фраз.
//Используй класс Person из "общих рекомендаций" ниже. Добавь в этот класс три инфиксные функции:
    //says должна принимать строку, добавлять её в список фраз и возвращать этот же объект Person для дальнейшей работы.
    // Всегда вызывается первой.
    //and работает так же как и says, но не может быть вызвана первой (в этом случае нужно выкидывать IllegalStateException).
    //or должна принимать строку и заменять последнюю фразу в списке фраз, выбирая случайным образом переданную строку или
        // последнюю фразу из списка фраз с помощью метода selectPhrase. Так же должна возвращать текущий объект Person для дальнейшей работы.
        // Так же не может быть вызвана первой, иначе выбрасывает IllegalStateException.


class Person(private val name: String) {

    private val phrases = mutableListOf<String>()

    fun print() {
        println(phrases.joinToString(" "))
    }

    private fun selectPhrase(first: String, second: String): String {
        val random = Random.nextInt(0, 2)
        return if (random == 0) first else second
    }

    infix fun says(phrase: String): Person {
        phrases.add(phrase)
        return this
    }

    infix fun and(phrase: String): Person {
        if (phrases.isEmpty()) {
            throw IllegalStateException("фразу нельзя начинать с and")
        }
        phrases.add(phrase)
        return this
    }

    infix fun or(phrase: String): Person {
        if (phrases.isEmpty()) {
            throw IllegalStateException("фразу нельзя начинать с or")
        }
        val lastPhrase = phrases.last()
        val selected = selectPhrase(phrase, lastPhrase)
        phrases[phrases.size - 1] = selected
        return this
    }
}

fun main() {
    val andrew = Person("Andrew")
    andrew says "Hello" and "brothers." or "sisters." and "I believe" and "you" and "can do it" or "can't"
    andrew.print()
}