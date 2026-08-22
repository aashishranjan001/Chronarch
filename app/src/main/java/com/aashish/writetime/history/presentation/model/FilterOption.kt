package com.aashish.writetime.history.presentation.model

sealed class FilterOption {
    sealed class SessionFilter: FilterOption() {
        sealed class DurationType: SessionFilter() {
            data object ShortType: DurationType()
            data object LongType: DurationType()

            companion object{
                val entries by lazy {
                    listOf(ShortType, LongType)
                }
            }
        }

        sealed class CompletionStatus: SessionFilter() {
            data object Finished: CompletionStatus()
            data object Cancelled: CompletionStatus()

            companion object {
                val entries by lazy {
                    listOf(Finished, Cancelled)
                }
            }
        }

        sealed class Date: SessionFilter() {
            data object ThisWeek: Date()
            data object ThisMonth: Date()
            data object Custom: Date()

            companion object {
                val entries by lazy {
                    listOf(ThisWeek, ThisMonth, Custom)
                }
            }
        }
    }

    sealed class TransactionFilter: FilterOption() {
        sealed class Type: TransactionFilter() {
            data object TaskCredit: Type()
            data object Bonus: Type()
            data object Redemption: Type()

            companion object {
                val entries by lazy {
                    listOf(TaskCredit, Bonus, Redemption)
                }
            }
        }

        sealed class Date: TransactionFilter() {
            data object ThisWeek: Date()
            data object ThisMonth: Date()
            data object Custom: Date()

            companion object {
                val entries by lazy {
                    listOf(ThisWeek, ThisMonth, Custom)
                }
            }
        }
    }
}