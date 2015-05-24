package com.exorath.game.api.kits;

import com.exorath.game.api.Property;

/**
 * Created by too on 23/05/2015.
 * This is an enum with all default kit property keys.
 */
public class KitProperty {
    
    public static final Property
            
            NAME = Property.get( "name", "The name of this kit", Kit.DEFAULT_NAME ),
            MATERIAL = Property.get( "material", "The material that will be shown in inventories", Kit.DEFAULT_MATERIAL ),
            DESCRIPTION = Property.get( "description", "The description of the kit.", Kit.DEFAULT_DESCRIPTION ),
            UNLOCK_REQUIREMENTS = Property.get( "unlockedwith", "The kit might have some unlock requirements", null );
    
}
