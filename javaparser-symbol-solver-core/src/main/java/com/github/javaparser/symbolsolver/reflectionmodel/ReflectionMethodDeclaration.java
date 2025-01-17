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

package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.Context;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.TypeVariableResolutionCapability;
import com.github.javaparser.symbolsolver.declarations.common.MethodDeclarationCommonLogic;
import com.github.javaparser.symbolsolver.utils.ModifierUtils;
import com.github.javaparser.symbolsolver.utils.ResolvedAnnotationsUtil;
import com.github.javaparser.utils.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Federico Tomassetti
 */
public class ReflectionMethodDeclaration implements ResolvedMethodDeclaration, TypeVariableResolutionCapability {

    private Method method;
    private TypeSolver typeSolver;

    public ReflectionMethodDeclaration(Method method, TypeSolver typeSolver) {
        this.method = method;
        if (method.isSynthetic() || method.isBridge()) {
            throw new IllegalArgumentException();
        }
        this.typeSolver = typeSolver;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean isField() {
        return false;
    }

    @Override
    public boolean isParameter() {
        return false;
    }

    @Override
    public String toString() {
        return "ReflectionMethodDeclaration{" +
                "method=" + method +
                '}';
    }

    @Override
    public boolean isType() {
        return false;
    }

    @Override
    public ResolvedReferenceTypeDeclaration declaringType() {
        if (method.getDeclaringClass().isInterface()) {
            return new ReflectionInterfaceDeclaration(method.getDeclaringClass(), typeSolver);
        }
        if (method.getDeclaringClass().isEnum()) {
            return new ReflectionEnumDeclaration(method.getDeclaringClass(), typeSolver);
        }
        return new ReflectionClassDeclaration(method.getDeclaringClass(), typeSolver);
    }

    @Override
    public ResolvedType getReturnType() {
        return ReflectionFactory.typeUsageFor(method.getGenericReturnType(), typeSolver);
    }

    @Override
    public int getNumberOfParams() {
        return method.getParameterTypes().length;
    }

    @Override
    public ResolvedParameterDeclaration getParam(int i) {
        boolean variadic = false;
        if (method.isVarArgs()) {
            variadic = i == (method.getParameterCount() - 1);
        }

        Annotation[] tempAnnotations = method.getParameterAnnotations()[i];

        return new ReflectionParameterDeclaration(method.getParameterTypes()[i], method.getGenericParameterTypes()[i],
                typeSolver, variadic, method.getParameters()[i].getName(), tempAnnotations);
    }

    @Override
    public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
        return Arrays.stream(method.getTypeParameters()).map((refTp) -> new ReflectionTypeParameter(refTp, false, typeSolver)).collect(Collectors.toList());
    }

    public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
        return new MethodDeclarationCommonLogic(this, typeSolver).resolveTypeVariables(context, parameterTypes);
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean isDefaultMethod() {
        return method.isDefault();
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public AccessSpecifier accessSpecifier() {
        return ReflectionFactory.modifiersToAccessLevel(this.method.getModifiers());
    }

    @Override
    public int getNumberOfSpecifiedExceptions() {
        return this.method.getExceptionTypes().length;
    }

    @Override
    public ResolvedType getSpecifiedException(int index) {
        if (index < 0 || index >= getNumberOfSpecifiedExceptions()) {
            throw new IllegalArgumentException();
        }
        return ReflectionFactory.typeUsageFor(this.method.getExceptionTypes()[index], typeSolver);
    }

    @Override
    public String toDescriptor() {
        return TypeUtils.getMethodDescriptor(method);
    }

    @Override
    public boolean hasModifier(com.github.javaparser.ast.Modifier.Keyword keyword) {
        return ModifierUtils.hasModifier(method, method.getModifiers(), keyword);
    }

    @Override
    public List<? extends ResolvedAnnotation> getAnnotations() {
        return ResolvedAnnotationsUtil.getAnnotations(method, typeSolver);
    }

    @Override
    public Set<ResolvedAnnotationDeclaration> getDeclaredAnnotations() {
        return ResolvedAnnotationsUtil.getDeclaredAnnotations(method, typeSolver);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReflectionMethodDeclaration that = (ReflectionMethodDeclaration) o;

        if (!method.equals(that.method)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
