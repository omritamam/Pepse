package pepse.world.trees;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;

public class LeafDensityFactory {

    private final int leafSlots;
    private final int seed;
    private static final int TREE_KINDS = 4;
    private static final Color[] BASE_COLORS = new Color[] {new Color(50, 200, 30),
            new Color(131, 38, 22),
            new Color(155, 108, 69)};

    public LeafDensityFactory(int leafSlots, int seed){
        this.leafSlots = leafSlots;
        this.seed = seed;
    }

    public Color getColor(float x){
        return BASE_COLORS[pick(x, true)];
    }

    public BiPredicate<Integer, Integer> getDensity(float x){
        switch (pick(x, false)){
            case 0: // plus shape
                return (i, j)->{
                    return (Math.abs(leafSlots / 2 - i) < (leafSlots / 2) - 1) ||
                            (Math.abs(leafSlots / 2 - j) < (leafSlots / 2) - 1);
                };
            case 1: // tall shape
                return (i, j)-> {
                    return (Math.abs(leafSlots / 3 - (i - 1)) < (leafSlots / 3));
                };
            case 2: // triangle shape
                return (i, j)->{
                    return ((Math.abs(leafSlots - i)) > (Math.abs(leafSlots - j)) / 2)
                            && ((Math.abs(i + 1)) > (Math.abs(leafSlots - j)) / 2);
                };
            default:    // square shape
                return (i, j)->(true);
        }
    }

    private int pick(float x, boolean color){
        int range;
        if (color){
            range = BASE_COLORS.length;
        }
        else {
            range = TREE_KINDS;
        }
        return new Random(Objects.hash(x, this.seed)).nextInt(range);
    }
}
