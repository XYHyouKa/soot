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

import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents aggregated method parameters information and aims at generating
 * MethodParameters attribute of a method.<br/><br/>
 *
 * Recommend use {@link AggregatedMethodParametersTag#of(String[], MethodParametersTag)} to instantiate.<br/>
 * ParameterNames array is collected from LocalVariable(&Type)Table, and the aggregation logic is:<br/>
 * 1.Once MethodParametersTag exists, use its data directly;<br/>
 * 2.Otherwise, use ParameterNames collected by Soot, each "access" is 0 by default;<br/>
 * 3.If none of them exists, the instance should not be created!!!
 */
public class AggregatedMethodParametersTag implements Tag {

  public static final String NAME = "AggregatedMethodParametersTag";

  private static final Logger logger = LoggerFactory.getLogger(AggregatedMethodParametersTag.class);
  private static final String EMPTY_WARN =
      "An empty AggregatedMethodParametersTag is created, please check usages of the constructor";

  private final MethodParametersTag.ParameterItem[] params;

  /**
   * Create an empty AggregatedMethodParametersTag. An empty attribute should not be attached to any method(host).
   */
  public AggregatedMethodParametersTag() {
    params = null;
    logger.warn(EMPTY_WARN);
  }

  /**
   * Recommend use {@link AggregatedMethodParametersTag#of(String[], MethodParametersTag)} instead,
   * or make sure the params are legal.
   *
   * @param mpTag requires nonnull (which means nonempty as well)
   */
  public AggregatedMethodParametersTag(MethodParametersTag mpTag) {
    params = mpTag.getParams();
    if (params.length == 0) {
      logger.warn(EMPTY_WARN);
    }
  }

  /**
   * Recommend use {@link AggregatedMethodParametersTag#of(String[], MethodParametersTag)} instead,
   * or make sure the params are legal.
   *
   * @param parameterNames requires nonnull and non-fully-empty, i.e., should not contain only <code>null</code> values
   */
  public AggregatedMethodParametersTag(String[] parameterNames) {
    params = new MethodParametersTag.ParameterItem[parameterNames.length];
    int i = 0;
    for (String pn : parameterNames) {
      params[i++] = new MethodParametersTag.ParameterItem(pn, 0);
    }
    if (params.length == 0) {
      logger.warn(EMPTY_WARN);
    }
  }

  public static AggregatedMethodParametersTag of(String[] parameterNames, MethodParametersTag mpTag) {
    if (mpTag != null) {
      return new AggregatedMethodParametersTag(mpTag);
    }
    if (!isFullyEmpty(parameterNames)) {
      return new AggregatedMethodParametersTag(parameterNames);
    }
    return new AggregatedMethodParametersTag();
  }

  /**
   * Gets whether the given array is fully empty, i.e., contains only <code>null</code> values
   *
   * @param array
   *          The array to check
   * @return True if the given array contains only <code>null</code> values, false otherwise
   */
  private static boolean isFullyEmpty(String[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null && !array[i].isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Makes the given method visitor visit this tag.
   *
   * @param methodVisitor a method visitor.
   */
  public void accept(final MethodVisitor methodVisitor) {
    for (MethodParametersTag.ParameterItem p : params) {
      methodVisitor.visitParameter(p.name, p.access);
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public byte[] getValue() {
    throw new RuntimeException("AggregatedMethodParametersTag has no value for bytecode");
  }

  @Override
  public String toString() {
    return Arrays.toString(params);
  }
}
