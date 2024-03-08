package blackjack.view

import blackjack.model.Card
import blackjack.model.CardNumber
import blackjack.model.CardShape
import blackjack.model.Dealer
import blackjack.model.Participant
import blackjack.model.Participants
import blackjack.model.Player
import blackjack.model.WinningState

object OutputView {
    fun printInitialStatus(participants: Participants) {
        val dealer = participants.dealer
        val players = participants.players
        println()
        println(
            "${dealer.name}와 ${
                players.map { it.name }.joinToString(", ")
            }에게 2장을 나눠줬습니다.",
        )
        println("${dealer.name} : ${cardToString(dealer.getCards().first())}")
        players.forEach { println("${it.name}카드: ${it.getCards().joinToString(", ") { cardToString(it) }}") }
        println()
    }

    fun printParticipantStatus(participant: Participant) {
        when (participant) {
            is Player -> printPlayerStatus(participant)
            is Dealer -> printDealerStatus(participant)
        }
    }

    private fun printPlayerStatus(player: Player) {
        println("${player.name}카드 ${player.getCards().joinToString(", ") { cardToString(it) }}")
    }

    private fun printDealerStatus(dealer: Dealer) {
        val count = dealer.getCards().size
        println()
        if (count > 2) {
            println("${dealer.name}는 16이하라 ${count - 2}의 카드를 더 받았습니다.")
        }
    }

    fun printStatusAndScore(participants: Participants) {
        println()
        participants.getAllParticipants().forEach { participant ->
            println(
                "${participant.name}카드 ${
                    participant.getCards().joinToString(", ") { cardToString(it) }
                } - 결과: ${participant.calculateHandSum()}",
            )
        }
    }

    fun printResult(result: Map<Participant, WinningState>) {
        println("\n## 최종 승패")
        result.entries.reversed().forEach { (participant, winningState) ->
            when (participant) {
                is Dealer -> printDealerResult(participant, winningState, result.size - 1)
                is Player -> printParticipantResult(participant, winningState)
            }
        }
    }

    private fun printDealerResult(
        dealer: Dealer,
        winningState: WinningState,
        playerCount: Int,
    ) {
        val drawCount = playerCount - (winningState.wins + winningState.losses)
        println("${dealer.name}: ${winningState.wins}승 ${winningState.losses}패 ${drawCount}무")
    }

    private fun printParticipantResult(
        participant: Participant,
        winningState: WinningState,
    ) {
        val resultMessage =
            when {
                winningState.wins == 1 -> "승"
                winningState.losses == 1 -> "패"
                else -> "무"
            }
        println("${participant.name}: $resultMessage")
    }

    private fun cardToString(card: Card): String {
        val cardNumber =
            when (card.number) {
                CardNumber.JACK -> "J"
                CardNumber.QUEEN -> "Q"
                CardNumber.KING -> "K"
                CardNumber.ACE -> "A"
                else -> card.number.value.toString()
            }

        val cardShape =
            when (card.shape) {
                CardShape.DIAMOND -> "다이아몬드"
                CardShape.HEART -> "하트"
                CardShape.SPADE -> "스페이드"
                CardShape.CLOVER -> "클로버"
            }
        return cardNumber + cardShape
    }
}
