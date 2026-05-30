package com.example.todosampleapp.feature.todoAdd

import org.junit.Test

class AddTodoInputItemTest {
    @Test
    fun `Both title and detail empty`() {
        // Verify that canSave returns false when both title and detail are empty strings ("").
        val sut = AddTodoInputItem("", "")
        assert(!sut.canSave)
    }

    @Test
    fun `Title empty and detail populated`() {
        // Verify that canSave returns false when the title is empty, even if the detail string is provided.
        val sut = AddTodoInputItem("", "hoge")
        assert(!sut.canSave)
    }

    @Test
    fun `Title populated and detail empty`() {
        // Verify that canSave returns false when the detail is empty, even if the title string is provided.
        val sut = AddTodoInputItem("hoge", "")
        assert(!sut.canSave)
    }

    @Test
    fun `Both title and detail populated`() {
        // Verify that canSave returns true when both title and detail contain at least one character.
        val sut = AddTodoInputItem("hoge", "hoge")
        assert(sut.canSave)
    }

    @Test
    fun `Whitespace only strings`() {
        // Verify that canSave returns true when strings contain only whitespace, as isEmpty()
        // returns false for strings containing spaces, tabs, or newlines.
        val sut = AddTodoInputItem(" ", " ")
        assert(sut.canSave)
    }

    @Test
    fun `Companion object initial state check`() {
        // Verify that the 'initial' companion object property results in canSave returning false.
        val sut = AddTodoInputItem.initial
        assert(!sut.canSave)
    }

    @Test
    fun `Single character boundary test`() {
        // Verify that canSave returns true when both strings have a length of exactly one character.
        val sut = AddTodoInputItem("t", "t")
        assert(sut.canSave)
    }

    @Test
    fun `Special characters and emoji support`() {
        // Verify that canSave returns true when strings consist of special symbols,
        // non-Latin characters, or emojis.
        val sut = AddTodoInputItem("😃", "🥹")
        assert(sut.canSave)
    }

    @Test
    fun `Large string input check`() {
        // Verify that canSave correctly evaluates to true when handling very large string
        // inputs for both title and detail fields.
        val sut =
            AddTodoInputItem(
                "lsjフィぁ絵jlfj死jfぁ生fジャlsjfあせfぃせjfぁせいfじゃせlふぃlさふぃあ",
                "lsjfぃあせjfぁjsジェフィぁsjfぁいsjfぃあせjflsジェファsぃfjsぁいえjfぃせjfぃあj背fぁい",
            )
        assert(sut.canSave)
    }
}
