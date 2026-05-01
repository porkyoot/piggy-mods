package is.pig.minecraft.api;

import java.util.List;
import java.util.Collections;

/**
 * Interface for any object that wishes to be displayed in a Radial Menu.
 */
public interface RadialMenuItem {
    /**
     * Returns the identifier of the icon texture.
     */
    String getIconId(boolean isSelected);

    /**
     * Returns the display name of the item.
     */
    String getDisplayName();
    
    /**
     * Returns sub-menu items if any.
     */
    default List<? extends RadialMenuItem> getSubMenuItems() {
        return Collections.emptyList();
    }
}
