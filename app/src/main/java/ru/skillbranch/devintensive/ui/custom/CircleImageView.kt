package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils.dpToPx
import ru.skillbranch.devintensive.utils.Utils.pxToDp


open class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var cvBorderColor = DEFAULT_BORDER_COLOR
    private var cvBorderWidth = dpToPx(DEFAULT_BORDER_WIDTH)
    private var defAvatar: Bitmap? = null
    private var srcDrawable: Drawable? = null
    private var text: String? = null
    private var lastTextSize: Int = 0

    init {
        if (attrs!=null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            cvBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_COLOR)
            cvBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, cvBorderWidth)
            a.recycle()
        }
    }

    fun getBorderWidth(): Int = pxToDp(cvBorderWidth)

    fun setBorderWidth(dp: Int) {
        cvBorderWidth = dpToPx(dp)
        drawBorder()
    }

    fun getBorderColor(): Int = cvBorderColor

    fun setBorderColor(hex: String) {
        cvBorderColor = Color.parseColor(hex)
        drawBorder()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        cvBorderColor = ContextCompat.getColor(App.applicationContext(), colorId)
        this.invalidate()
    }

    fun getSourceDrawable(): Drawable? = srcDrawable

    override fun setImageDrawable(drawable: Drawable?) {
        srcDrawable = drawable
        val img = getBitmapFromDrawable(drawable) ?: drawDefaultAvatar()
        super.setImageDrawable(getCircularDrawable(img))
    }

    override fun setImageResource(resId: Int) {
        srcDrawable = resources.getDrawable(resId, context.theme)
        val img = BitmapFactory.decodeResource(resources, resId)
        super.setImageDrawable(getCircularDrawable(img))
    }

    override fun setImageBitmap(bitmap: Bitmap?) {
        srcDrawable = BitmapDrawable(resources, bitmap)
        super.setImageDrawable(getCircularDrawable(bitmap))
    }

    private fun setDefaultAvatar() {
        srcDrawable = resources.getDrawable(R.drawable.avatar_default, context.theme)
        super.setImageResource(R.drawable.avatar_default)
    }

    fun drawAvatar(text: String?, size: Int) {
        if (text != this.text || size != this.lastTextSize) {
            if (text == null) setDefaultAvatar()
            else defAvatar = generateTextAvatar(text, size)
            this.text = text
            this.lastTextSize = size
            if (srcDrawable == null)
                super.setImageDrawable(BitmapDrawable(resources, drawBorder(defAvatar)))
        }
    }

    private fun getCircularDrawable(bitmap: Bitmap?): Drawable =
        RoundedBitmapDrawableFactory.create(resources, drawBorder(bitmap))
            .also {
                it.isCircular = true
            }

    private fun drawBorder(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        val radius = bitmap.width / 2f
        val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBorder.style = Paint.Style.STROKE
        paintBorder.strokeWidth = cvBorderWidth.toFloat()
        paintBorder.color = cvBorderColor
        val bitmapWithBorder = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmapWithBorder)
        canvas.drawCircle(radius, radius, radius - cvBorderWidth / 2f, paintBorder)
        return bitmapWithBorder
    }

    private fun drawDefaultAvatar(): Bitmap {
        val bitmap = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        val color = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, color, true)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.style = Paint.Style.FILL
            it.color = color.data
        }
        val radius = layoutParams.width / 2f
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius, paint)
        return bitmap
    }

    private fun generateTextAvatar(text: String, size: Int): Bitmap {
        srcDrawable = null
        val image = drawDefaultAvatar()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.textSize = size.toFloat()
            it.color = DEFAULT_BORDER_COLOR
            it.textAlign = Paint.Align.CENTER
        }
        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)
        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat())

        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()
        val canvas = Canvas(image)
        canvas.drawText(text, backgroundBounds.centerX(), textBottom, paint)
        return image
    }

    private fun drawBorder() {
        if (srcDrawable != null)
            setImageDrawable(srcDrawable)
        else
            super.setImageDrawable(BitmapDrawable(resources, drawBorder(defAvatar)))
    }

    private fun getBitmapFromDrawable(drawable: Drawable?) = when (drawable) {
        null -> null
        is BitmapDrawable -> drawable.bitmap
        else -> try {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}