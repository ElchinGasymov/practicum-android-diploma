package ru.practicum.android.diploma.util.adapter

// Функция для форматирования диапазона зарплат в виде строки
fun formatValueSalary(salaryFrom: Int?, salaryTo: Int?): String {
    return when {
        salaryFrom != null && (salaryTo == null || salaryTo == 0) -> "от ${toSalaryString(salaryFrom)}"
        (salaryFrom == 0 || salaryFrom == null) && salaryTo != null && salaryTo != 0 -> "до ${toSalaryString(salaryTo)}"
        salaryFrom != null && salaryFrom != 0 && salaryTo != null && salaryTo != 0 -> "от ${toSalaryString(salaryFrom)} до ${toSalaryString(salaryTo)}"
        else -> "Зарплата не указана"
    }
}

// Функция для форматирования числа зарплаты с добавлением пробелов каждые три цифры
fun toSalaryString(salary: Int): String {
    return "%,d".format(salary).replace(',', ' ')
}


// Функция для конвертации зарплаты в строку с указанием валюты
fun converterSalaryToString(salaryFrom: Int?, salaryTo: Int?, currency: String?): String {
    val formattedSalary = formatValueSalary(salaryFrom, salaryTo)
    return when (currency) {
        "USD" -> "$formattedSalary €"
        "EUR" -> "$formattedSalary $"
        "RUR" -> "$formattedSalary ₽"
        "AZN" -> "$formattedSalary ₼"
        "BYR" -> "$formattedSalary Br"
        "GEL" -> "$formattedSalary ₾"
        "KGS" -> "$formattedSalary с"
        "KZT" -> "$formattedSalary ₸"
        "UAH" -> "$formattedSalary ₴"
        "UZS" -> "$formattedSalary Soʻm"
        else -> formattedSalary
    }
}
