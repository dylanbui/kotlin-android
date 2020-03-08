package com.dylanbui.android_ui_custom.expandable_check_recyclerview.listeners;


import com.dylanbui.android_ui_custom.expandable_check_recyclerview.models.CheckedExpandableGroup;

public interface OnChildrenCheckStateChangedListener {

  /**
   * @param firstChildFlattenedIndex the flat position of the first child in the {@link
   * CheckedExpandableGroup}
   * @param numChildren the total number of children in the {@link CheckedExpandableGroup}
   */
  void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren);
}
