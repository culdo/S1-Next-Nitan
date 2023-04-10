package me.ykrank.s1next.view.page.post.internal

import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.ykrank.s1next.R

abstract class PostToolsExtra(@DrawableRes val icon: Int, @StringRes val name: Int) {

    abstract fun onClick(editText: EditText)
}

class PostToolsExtraBold : PostToolsExtra(R.drawable.ic_bold, R.string.bold) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)
        editText.text.replace(start, end, "[b]$selectText[/b]")
    }

}

class PostToolsExtraItalic : PostToolsExtra(R.drawable.ic_italic, R.string.italic) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)
        editText.text.replace(start, end, "[i]$selectText[/i]")
    }

}

class PostToolsExtraUnderline : PostToolsExtra(R.drawable.ic_underline, R.string.underline) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)
        editText.text.replace(start, end, "[u]$selectText[/u]")
    }

}

class PostToolsExtraImg : PostToolsExtra(R.drawable.ic_image, R.string.image) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)

        editText.text.replace(start, end, "[img]$selectText[/img]")
    }

}

class PostToolsExtraLink : PostToolsExtra(R.drawable.ic_link, R.string.link) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)

        editText.text.replace(start, end, "[url=]$selectText[/url]")
    }

}

class PostToolsExtraStrikethrough : PostToolsExtra(R.drawable.ic_strikethrough, R.string.strike_through) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)

        editText.text.replace(start, end, "[s]$selectText[/s]")
    }

}

class PostToolsExtraQuote : PostToolsExtra(R.drawable.ic_quote, R.string.quote) {
    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)

        editText.text.replace(start, end, "[quote]$selectText[/quote]")
    }
}

class PostToolsExtraCreditPermission : PostToolsExtra(R.drawable.ic_lock, R.string.credit_permission) {

    override fun onClick(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selectText = editText.text.substring(start, end)

        editText.text.replace(start, end, "[hide=积分数]$selectText[/hide]")
    }

}