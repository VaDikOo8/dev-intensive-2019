package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val namePattern = "^[А-ЯA-Z][а-яa-z]*(\\s[А-ЯA-Zа-яa-z]*)?(\\s[А-ЯA-Zа-яa-z]*)?".toRegex()
        val professionPattern = "[а-яa-z ]*".toRegex()
        val materialPattern = "^(\\D+)\$".toRegex()
        val birthdayPattern = "^(\\d+)\$".toRegex()
        val serialPattern = "^(\\d{7})\$".toRegex()

        return when (question) {
                Question.NAME -> if (!answer.matches(namePattern)) {
                    "Имя должно начинаться с заглавной буквы\n${question.question}" to status.color
                } else checkAnswer(answer)
                Question.PROFESSION -> if (!answer.matches(professionPattern)) {
                    "Профессия должна начинаться со строчной буквы\n" +
                        "${question.question}" to status.color
                } else checkAnswer(answer)
                Question.MATERIAL -> if (!answer.matches(materialPattern)) {
                    "Материал не должен содержать цифр\n" +
                        "${question.question}" to status.color
                } else checkAnswer(answer)
                Question.BDAY -> if (!answer.matches(birthdayPattern)) {
                    "Год моего рождения должен содержать только цифры\n" +
                        "${question.question}" to status.color
                } else checkAnswer(answer)
                Question.SERIAL -> if (!answer.matches(serialPattern)) {
                    "Серийный номер содержит только цифры, и их 7\n" +
                        "${question.question}" to status.color
                } else checkAnswer(answer)
                Question.IDLE -> question.question to status.color
            }
    }

    private fun checkAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            status = status.nextStatus()
            if (status != Status.NORMAL) {
                "Это неправильный ответ\n${question.question}" to status.color
            } else {
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values() [this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}