package com.github.masahito.kotestsandbox.businesslogics

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.forAll
import java.time.LocalDate


class BusinessLogicTests : FunSpec({

    test("空ではないリストの合計はtailの合計にheadを足したもの") {
        forAll<Int, List<Int>> { head, tail ->
            ListUtil.sum(0, listOf(head) + tail) == head + ListUtil.sum(0, tail)
        }
    }

    test("金額は5000円より高い") {
        val yenArb = arb { rs ->
            val nums = Arb.long(LongRange(5001, 10000)).values(rs)
            nums.map { num -> Yen(num.value) }
        }
        forAll<Yen>(yenArb) { yen1 ->
            yen1.amount > 5000L
        }
    }


    test("全てのユーザは必ず空文字ではない名前を持つ") {
        val userArb = arb { rs ->
            val names = Arb.string(minSize = 1).values(rs)
            val ages = Arb.int().values(rs)
            names.zip(ages).map { (name, age) ->
                User(
                    name.value,
                    age.value
                )
            }
        }
        forAll(userArb) { user ->
            user.name.isNotEmpty()
        }
    }

    test("クレジットの期限が今日より前ならば、決済結果は期限切れとなる") {
        val today = LocalDate.of(2020, 6, 1)
        val expiredCardArb: Arb<CreditCard> = arb { rs ->
            val nums = Arb.int(0, 9999).values(rs)
            val holders = Arb.string(minSize = 1).values(rs)
            val diffs = Arb.long(1, 24).values(rs)
            nums.flatMap { num ->
                diffs.flatMap { diff ->
                    holders.map { holder ->
                        val numStr = num.value.toString().padStart(4, '0')
                        CreditCard(
                            number = "${numStr}-${numStr}-${numStr}-${numStr}",
                            limit = today.minusMonths(diff.value),
                            holder = holder.value
                        )
                    }
                }
            }
        }

        val orderArb: Arb<Order> = arb { rs ->
            val ids = Arb.long(0L).values(rs)
            val names = Arb.string(1, 24).values(rs)
            ids.flatMap { id ->
                names.map { name ->
                    Order(
                        Item(
                            id = id.value,
                            name = name.value
                        )

                    )
                }
            }
        }
        forAll<CreditCard, Order>(expiredCardArb, orderArb) { card, order ->
            card.charge(order) == ChargeResult.Expired

        }
    }
})

