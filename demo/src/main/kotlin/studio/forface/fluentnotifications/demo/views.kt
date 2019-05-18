package studio.forface.fluentnotifications.demo

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible

class ExpandableLinearLayout @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : LinearLayout( context, attrs, defStyleAttr ) {

    private val header by lazy { getChildAt(0 ) as ExpandableLinearLayoutHeader }

    private var state: State = State.Collapsed
        set( value ) {
            field = value
            header.state = field
            children.filterIndexed { index, _ -> index != 0 }
                .forEach { it.isVisible = field == State.Expanded }
        }

    init {
        doOnPreDraw {
            state = State.Collapsed
            header.setOnClickListener {
                state = when ( state ) {
                    State.Expanded -> State.Collapsed
                    State.Collapsed -> State.Expanded
                }
            }
        }
    }
}

class ExpandableLinearLayoutHeader @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : TextView( context, attrs, defStyleAttr ) {

    var state: State = State.Collapsed
        set( value ) {
            field = value
            setDrawableRight( when ( state ) {
                State.Collapsed -> R.drawable.ic_arrow_down
                State.Expanded -> R.drawable.ic_arrow_up
            } )
        }

    private fun setDrawableRight( @DrawableRes drawableRes: Int ) =
        setCompoundDrawablesWithIntrinsicBounds(0,0, drawableRes,0 )
}

enum class State { Collapsed, Expanded }
