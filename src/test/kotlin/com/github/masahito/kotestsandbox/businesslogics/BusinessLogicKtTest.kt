package com.github.masahito.kotestsandbox.businesslogics

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.forAll
import java.time.LocalDate


class BusinessLogicTests : FunSpec({
    context("ListUtil.sum") {
        test("空のリストの合計は0") {
            ListUtil.sum(0, emptyList()).shouldBe(0)
        }
        test("空ではないリストの合計はtailの合計にheadを足したもの") {
            forAll<Int, List<Int>> { head, tail ->
                ListUtil.sum(0, listOf(head) + tail) == head + ListUtil.sum(0, tail)
            }
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
        val expiredCardArb: Arb<CreditCard> = Arb.bind(
            Arb.int(0, 9999),
            Arb.string(minSize = 1),
            Arb.long(1, 24)
        ) { num, holder, diff ->
            val numStr = num.toString().padStart(4, '0')
            CreditCard(
                number = "${numStr}-${numStr}-${numStr}-${numStr}",
                limit = today.minusMonths(diff),
                holder = holder
            )

        }

        val orderArb: Arb<Order> =
            Arb.bind(
                Arb.long(0L),
                Arb.string(1, 24)
            ) { a, b -> Order(Item(a, b)) }

        forAll<CreditCard, Order>(PropTestConfig(seed = 1), expiredCardArb, orderArb) { card, order ->
            card.charge(order) == ChargeResult.Expired
        }
    }
})

class PropertyExample: StringSpec({
    "String size" {
        forAll<String, String> (iterations = 10000){ a, b ->
            (a + b).length == a.length + b.length
        }
    }

    "String size2" {
        checkAll<String, String> { a, b ->
            (a + b).length shouldBe  a.length + b.length
        }
    }
})