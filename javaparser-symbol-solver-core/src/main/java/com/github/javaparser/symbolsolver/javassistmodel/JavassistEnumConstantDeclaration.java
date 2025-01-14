/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2023 The JavaParser Team.
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

package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedAnnotation;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.utils.ResolvedAnnotationsUtil;
import javassist.CtField;
import javassist.bytecode.AccessFlag;

import java.util.List;
import java.util.Set;

/**
 * @author Federico Tomassetti
 */
public class JavassistEnumConstantDeclaration implements ResolvedEnumConstantDeclaration {

    private CtField ctField;
    private TypeSolver typeSolver;
    private ResolvedType type;

    public JavassistEnumConstantDeclaration(CtField ctField, TypeSolver typeSolver) {
        if (ctField == null) {
            throw new IllegalArgumentException();
        }
        if ((ctField.getFieldInfo2().getAccessFlags() & AccessFlag.ENUM) == 0) {
            throw new IllegalArgumentException(
                    "Trying to instantiate a JavassistEnumConstantDeclaration with something which is not an enum field: "
                            + ctField.toString());
        }
        this.ctField = ctField;
        this.typeSolver = typeSolver;
    }


    @Override
    public String getName() {
        return ctField.getName();
    }

    @Override
    public ResolvedType getType() {
        if (type == null) {
            type = new ReferenceTypeImpl(new JavassistEnumDeclaration(ctField.getDeclaringClass(), typeSolver));
        }
        return type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "ctField=" + ctField.getName() +
                ", typeSolver=" + typeSolver +
                '}';
    }

    @Override
    public List<? extends ResolvedAnnotation> getAnnotations() {
        return ResolvedAnnotationsUtil.getAnnotations(ctField, typeSolver);
    }

    @Override
    public Set<ResolvedAnnotationDeclaration> getDeclaredAnnotations() {
        return ResolvedAnnotationsUtil.getDeclaredAnnotations(ctField, typeSolver);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavassistEnumConstantDeclaration that = (JavassistEnumConstantDeclaration) o;

        return ctField.equals(that.ctField);
    }

    @Override
    public int hashCode() {
        return ctField.hashCode();
    }
}
