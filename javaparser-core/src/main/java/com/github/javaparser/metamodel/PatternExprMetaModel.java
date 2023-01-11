/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2021 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Generated;
import com.github.javaparser.ast.expr.PatternExpr;

import java.util.Optional;

/**
 * This file, class, and its contents are completely generated based on:
 * <ul>
 *     <li>The contents and annotations within the package `com.github.javaparser.ast`, and</li>
 *     <li>`ALL_NODE_CLASSES` within the class `com.github.javaparser.generator.metamodel.MetaModelGenerator`.</li>
 * </ul>
 *
 * For this reason, any changes made directly to this file will be overwritten the next time generators are run.
 */
@Generated("com.github.javaparser.generator.metamodel.NodeMetaModelGenerator")
public class PatternExprMetaModel extends ExpressionMetaModel {

    @Generated("com.github.javaparser.generator.metamodel.NodeMetaModelGenerator")
    PatternExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
        super(superBaseNodeMetaModel, PatternExpr.class, "PatternExpr", "com.github.javaparser.ast.expr", false, false);
    }

    public PropertyMetaModel modifiersPropertyMetaModel;

    public PropertyMetaModel namePropertyMetaModel;

    public PropertyMetaModel typePropertyMetaModel;
}
