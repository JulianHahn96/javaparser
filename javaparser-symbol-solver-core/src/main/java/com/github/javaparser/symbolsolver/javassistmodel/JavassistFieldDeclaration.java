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

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.utils.ModifierUtils;
import com.github.javaparser.symbolsolver.utils.ResolvedAnnotationsUtil;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Federico Tomassetti
 */
public class JavassistFieldDeclaration implements ResolvedFieldDeclaration {
    private CtField ctField;
    private TypeSolver typeSolver;

    public JavassistFieldDeclaration(CtField ctField, TypeSolver typeSolver) {
        this.ctField = ctField;
        this.typeSolver = typeSolver;
    }

    @Override
    public ResolvedType getType() {
        try {
            String signature = ctField.getGenericSignature();
            if (signature == null) {
                signature = ctField.getSignature();
            }
            SignatureAttribute.Type genericSignatureType = SignatureAttribute.toTypeSignature(signature);
            return JavassistUtils.signatureTypeToType(genericSignatureType, typeSolver, (ResolvedTypeParametrizable) declaringType());
        } catch (BadBytecode e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(ctField.getModifiers());
    }
    
    @Override
    public boolean isVolatile() {
        return Modifier.isVolatile(ctField.getModifiers());
    }

    @Override
    public String getName() {
        return ctField.getName();
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public boolean isParameter() {
        return false;
    }

    @Override
    public boolean isType() {
        return false;
    }

    @Override
    public AccessSpecifier accessSpecifier() {
        return JavassistFactory.modifiersToAccessLevel(ctField.getModifiers());
    }

    @Override
    public ResolvedTypeDeclaration declaringType() {
        return JavassistFactory.toTypeDeclaration(ctField.getDeclaringClass(), typeSolver);
    }

    @Override
    public boolean hasModifier(com.github.javaparser.ast.Modifier.Keyword keyword) {
        return ModifierUtils.hasModifier(ctField, ctField.getModifiers(), keyword);
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
    public Object constantValue() {
        if(Modifier.isStatic(ctField.getModifiers()) && Modifier.isFinal(ctField.getModifiers())) {
            return ctField.getConstantValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavassistFieldDeclaration that = (JavassistFieldDeclaration) o;

        return ctField.equals(that.ctField);
    }

    @Override
    public int hashCode() {
        return ctField.hashCode();
    }
}
