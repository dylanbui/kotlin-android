package com.dylanbui.routerapp.external_activity.models

import com.dylanbui.android_ui_custom.expandable_recyclerview.models.ExpandableGroup

class Genre(title: String?, items: List<Artist?>?, val iconResId: Int) : ExpandableGroup<Artist?>(title, items) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Genre) return false
        return iconResId == other.iconResId
    }


    override fun hashCode(): Int {
        return iconResId
    }

}


