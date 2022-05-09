import kotlin.system.exitProcess

abstract class Gamer() {
    abstract fun guessRandomNumber()
    abstract fun generateRandomNumber()
    abstract var hiddenNumber: Int
    abstract var guessedNumber: Int
}

class Player : Gamer() {
    override var hiddenNumber = 0
    override var guessedNumber = 0
    var userNickName = ""
    private var currentGuess = 0
    fun choiceNickName() {
        println("Максимальная длинна 8")
        print("Выберите себе никнейм: ")
        userNickName = readLine().toString()
        if (userNickName.length > 8) {
            "Слишком длинный никнейм\n".gameSay()
            choiceNickName()
        }
    }

    override fun guessRandomNumber() {
        print("$userNickName: Я думаю это ")
        guessedNumber = readLine()?.toInt() ?: 0
    }

    override fun generateRandomNumber() {
        if (currentGuess == 0) {
            print("$userNickName: Я загадываю число ")
            currentGuess++
        } else {
            print("$userNickName: Теперь Я загадываю число ")
        }
        hiddenNumber = readLine()?.toInt() ?: 0
        if (hiddenNumber > 100 || hiddenNumber < 0) {
            "Неправильное значение".gameSay()
            generateRandomNumber()
        }
    }
}

class Computer : Gamer() {
    override var hiddenNumber = 0
    override var guessedNumber = 0
    override fun guessRandomNumber() {
        guessedNumber = (0..100).random()
        "Я думаю это: $guessedNumber".computerSay()
        Thread.sleep(300L)
    }

    override fun generateRandomNumber() {
        hiddenNumber = (0..100).random()
        "Я загадал число, попытайтесь угадать!".computerSay()
        println("TEST NUMBER IS: $hiddenNumber")
    }
}

class GameProccesByUser(
    var counterGames: Int = 0,
    var player: Player,
    var computer: Computer
) {
    fun gameByUser() {
        computer.generateRandomNumber()
        while (true) {
            player.guessRandomNumber()
            when {
                (player.guessedNumber < computer.hiddenNumber) -> "Это число меньше от загаданого".gameSay()
                (player.guessedNumber > computer.hiddenNumber) -> "Это число больше от загаданого".gameSay()
                (player.guessedNumber == computer.hiddenNumber) -> {
                    "Поздравляю ты угадал! Загаданное число было ${computer.hiddenNumber}\n"
                        .computerSay()
                    counterGames++
                    break
                }
            }
        }
    }
}

class GameProccessByComputer(
    private var player: Player,
    private var computer: Computer
) {
    fun gameByComputer() {
        println("Компьютер понимает такие команды как: Больше, Меньше, Угадал")
        var userAnswer = ""
        player.generateRandomNumber()
        computer.guessRandomNumber()
        while (true) {
            print("${player.userNickName}: ")
            userAnswer = readLine().toString()
            when (userAnswer) {
                "Больше", "больше" -> {
                    when {
                        (computer.guessedNumber == player.hiddenNumber) -> {
                            "Вообще то он угадал! Играйте честно!".gameSay()
                            break
                        }
                        (computer.guessedNumber < player.hiddenNumber) -> {
                            "Вы играете не по правилам! Отвечайте честно! Очередь переходит к computer".gameSay()
                            break
                        }
                        (computer.guessedNumber > player.hiddenNumber) -> {
                            println("\n${player.userNickName}: Ваше число $userAnswer чем загаданное")
                            computer.guessRandomNumber()
                        }
                    }
                }
                "Меньше", "меньше" -> {
                    when {
                        (computer.guessedNumber == player.hiddenNumber) -> {
                            "Вообще то он угадал! Играйте честно!".gameSay()
                            break
                        }
                        (computer.guessedNumber > player.hiddenNumber) -> {
                            "Вы играете не по правилам! Отвечайте честно! Очередь переходит к computer".gameSay()
                            break
                        }
                        (computer.guessedNumber < player.hiddenNumber) -> {
                            println("${player.userNickName}: Ваше число $userAnswer чем загаданное")
                            computer.guessRandomNumber()
                        }
                    }
                }
                "Угадал", "угадал" -> {
                    when {
                        (computer.guessedNumber == player.hiddenNumber) -> {
                            println("\nПоздравляю Computer ты угадал! Загаданное число было ${computer.guessedNumber}\n")
                            break
                        }
                        (computer.guessedNumber != player.hiddenNumber) ->
                            "Ты играешь не по правилам! Он не угадал!".gameSay()
                    }
                }
                else -> {
                    "Неверное значение".gameSay()
                }
            }
        }
    }
}

class Game {
    private var enterGame = "play"
    private var playerTurn = "player"
    private var computerTurn = "comp"
    private var startGame = ""
    private var startRole = ""
    private var counterGames = 0
    private var player = Player()
    private val computer = Computer()
    private fun initGame() {
        println("Добро пожаловать в игру Угадай цифру")
        print("Для старта игры введите play: ")
        startGame = readLine().toString()
        println()
        if (startGame != "play") {
            println("Неверное значение $startGame")
            initGame()
        }
        player.choiceNickName()
        println()
    }

    private fun changeRole(role: String) {
        startRole = role
    }

    private fun choiceRole() {
        "Выберите кто первый будет играть\n".gameSay()
        "Введите player если хотите играть Вы".gameSay()
        "Введите comp если хотите что бы играл Computer".gameSay()
        print("Input: ")
        startRole = readLine().toString()
        if (startRole != "player" && startRole != "comp") {
            "Неверное значение $startRole".gameSay()
            choiceRole()
        }
        println()
    }

    private val gameProcessByUser = GameProccesByUser(
        counterGames = counterGames,
        player = player,
        computer = computer
    )
    private val gameProcessByComputer = GameProccessByComputer(
        player = player,
        computer = computer
    )

    fun startGame() {
        initGame()
        choiceRole()
        if (startGame == enterGame) {
            while (startGame == enterGame) {
                if (startRole == playerTurn) {
                    gameProcessByUser.gameByUser()
                    changeRole(computerTurn)
                }
                if (startRole == computerTurn) {

                    gameProcessByComputer.gameByComputer()
                    changeRole(playerTurn)
                }
            }
        } else {
            exitProcess(0)
        }
    }
}

fun main() {
    val game = Game()
    game.startGame()
}

fun String.computerSay() {
    println("\nComputer: $this")
}

fun String.gameSay() {
    println("The Game: $this")
}

