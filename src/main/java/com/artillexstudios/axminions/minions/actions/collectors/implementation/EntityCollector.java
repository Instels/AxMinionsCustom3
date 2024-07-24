package com.artillexstudios.axminions.minions.actions.collectors.implementation;

import com.artillexstudios.axminions.minions.Minion;
import com.artillexstudios.axminions.minions.actions.collectors.Collector;
import com.artillexstudios.axminions.minions.actions.collectors.CollectorShape;
import com.artillexstudios.axminions.minions.actions.filters.Filter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import redempt.crunch.CompiledExpression;

import java.util.List;
import java.util.function.Consumer;

public class EntityCollector extends Collector<Entity> {
    private final CollectorShape shape;
    private final CompiledExpression expression;
    private final List<Filter<?>> filters;

    public EntityCollector(CollectorShape shape, CompiledExpression expression, List<Filter<?>> filters) {
        this.shape = shape;
        this.expression = expression;
        this.filters = filters;
    }

    @Override
    public Class<?> getCollectedClass() {
        return Entity.class;
    }

    @Override
    public void collect(Minion minion, Consumer<Entity> consumer) {
        double radius = this.expression.getVariableCount() == 0 ? this.expression.evaluate() : this.expression.evaluate(minion.level().id());
        Location location = minion.location();

        this.shape.getEntities(location, radius, this.filters, consumer);
    }
}