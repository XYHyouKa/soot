package soot.tagkit;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2024 XYHyouKa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.ParameterNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents MethodParameters attribute of a method.
 */
public class MethodParametersTag implements Tag {

  public static final String NAME = "MethodParametersTag";

  private static final Logger logger = LoggerFactory.getLogger(MethodParametersTag.class);

  private final ParameterItem[] params;

  public MethodParametersTag(List<ParameterNode> parameters) {
    params = new ParameterItem[parameters.size()];
    int i = 0;
    for (ParameterNode parameter : parameters) {
      params[i++] = new ParameterItem(parameter.name, parameter.access);
    }
    if (params.length == 0) {
      logger.warn("An empty MethodParametersTag is created, please check usages of the constructor");
    }
  }

  public ParameterItem[] getParams() {
    return params;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public byte[] getValue() {
    throw new RuntimeException("MethodParametersTag has no value for bytecode");
  }

  @Override
  public String toString() {
    return Arrays.toString(params);
  }

  public static class ParameterItem {

    protected String name;
    protected int access;

    public ParameterItem(String name, int access) {
      this.name = name;
      this.access = access;
    }

    @Override
    public String toString() {
      return '{' + name + ", " +
          access + '}';
    }
  }
}
