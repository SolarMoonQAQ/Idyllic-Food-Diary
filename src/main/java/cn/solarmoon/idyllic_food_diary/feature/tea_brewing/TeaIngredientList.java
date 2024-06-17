package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import java.util.ArrayList;
import java.util.List;

public class TeaIngredientList {

    private final List<TeaIngredient.Base> baseIngredients;
    private final List<TeaIngredient.Side> sideIngredients;
    private final List<TeaIngredient.Add> addIngredients;
    private final List<TeaIngredient> common;

    public TeaIngredientList() {
        baseIngredients = new ArrayList<>();
        sideIngredients = new ArrayList<>();
        addIngredients = new ArrayList<>();
        common = new ArrayList<>();
    }

    public void add(TeaIngredient teaIngredient) {
        switch (teaIngredient.getType()) {
            case BASE -> addBase((TeaIngredient.Base) teaIngredient);
            case ADD -> addAdd((TeaIngredient.Add) teaIngredient);
            case SIDE -> addSide((TeaIngredient.Side) teaIngredient);
        }
        common.add(teaIngredient);
    }

    public List<TeaIngredient.Add> getTeaIngredientsHasEffect() {
        List<TeaIngredient.Add> list = new ArrayList<>();
        list.addAll(addIngredients);
        list.addAll(sideIngredients);
        return list;
    }

    public boolean isEmpty() {
        return baseIngredients.isEmpty() && addIngredients.isEmpty() && sideIngredients.isEmpty();
    }

    public void clear() {
        baseIngredients.clear();
        addIngredients.clear();
        sideIngredients.clear();
    }

    public void addBase(TeaIngredient.Base base) {
        baseIngredients.add(base);
    }

    public void addSide(TeaIngredient.Side side) {
        sideIngredients.add(side);
    }

    public void addAdd(TeaIngredient.Add add) {
        addIngredients.add(add);
    }

    public List<TeaIngredient.Base> getBaseIngredients() {
        return baseIngredients;
    }

    public List<TeaIngredient.Add> getAddIngredients() {
        return addIngredients;
    }

    public List<TeaIngredient.Side> getSideIngredients() {
        return sideIngredients;
    }

    public List<TeaIngredient> getCommon() {
        return common;
    }

    @Override
    public String toString() {
        return "TeaIngredientList{" +
                "baseIngredients=" + baseIngredients +
                ", sideIngredients=" + sideIngredients +
                ", addIngredients=" + addIngredients +
                '}';
    }

}
