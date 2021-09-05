package com.example.myapplication.let

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myapplication.R

@BindingAdapter("urlImage")
fun ImageView.setUrlImage(url: String) {
    Glide.with(this).load(url)
        .thumbnail(0.1f)
        .into(this)
}

@BindingAdapter("urlImageRadius16")
fun ImageView.setUrlImageRadius16(url: String) {
    Glide.with(this).load(url)
        .thumbnail(0.1f)
        .transform(RoundedCorners(16))
        .into(this)
}

@BindingAdapter("htmlText")
fun TextView.setHtmlText(html: String?) {
    text = html?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) }
}

// 약관 동의 여부에 따른 배경색
@BindingAdapter("onAgreementBackground")
fun TextView.setOnNextBackground(isCompleted: Boolean) {
    if (!isCompleted) {
        background =
            ContextCompat.getDrawable(context, R.drawable.btn_blue_sex)
        setTextColor(ContextCompat.getColor(context, R.color.little_dark_gray))
    } else {
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_solid_dark_gray_333333_radius_8dp)
        setTextColor(ContextCompat.getColor(context, R.color.white))
    }
}


}