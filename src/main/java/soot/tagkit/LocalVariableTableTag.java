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

import org.objectweb.asm.tree.LocalVariableNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents local variable table and local variable type table in method code.
 * Be aware that start and end could be invalid after transformation, so they are ignored.<br/><br/>
 *
 * Inside LocalVariableTable:<br/>
 * u2 local_variable_table_length;<br/>
 * {<br/>&nbsp&nbsp
 *   u2 start_pc;<br/>&nbsp&nbsp
 *   u2 length;<br/>&nbsp&nbsp
 *   u2 name_index;<br/>&nbsp&nbsp
 *   u2 descriptor_index;<br/>&nbsp&nbsp
 *   u2 index;<br/>
 * }<br/><br/>
 *
 * Inside LocalVariableTypeTable:<br/>
 * u2 local_variable_type_table_length;<br/>
 * {<br/>&nbsp&nbsp
 *   u2 start_pc;<br/>&nbsp&nbsp
 *   u2 length;<br/>&nbsp&nbsp
 *   u2 name_index;<br/>&nbsp&nbsp
 *   u2 signature_index;<br/>&nbsp&nbsp
 *   u2 index;<br/>
 * }
 */
public class LocalVariableTableTag implements Tag {

  public static final String NAME = "LocalVariableTableTag";

  private static final Logger logger = LoggerFactory.getLogger(LocalVariableTableTag.class);

  private final LocalVariableItem[] lvt;

  public LocalVariableTableTag(List<LocalVariableNode> localVariables) {
    lvt = new LocalVariableItem[localVariables.size()];
    int i = 0;
    for (LocalVariableNode lvn : localVariables) {
      lvt[i++] = new LocalVariableItem(lvn.name, lvn.desc, lvn.signature, lvn.index);
    }
    if (lvt.length == 0) {
      logger.warn("An empty LocalVariableTableTag is created, please check usages of the constructor");
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public byte[] getValue() {
    throw new RuntimeException("LocalVariableTableTag has no value for bytecode");
  }

  @Override
  public String toString() {
    return Arrays.toString(lvt);
  }

  public static class LocalVariableItem {

    protected final String name;
    protected final String desc;
    /** {@code signature} is nullable */
    protected final String signature;
    protected final int index;

    public LocalVariableItem(String name, String desc, String signature, int index) {
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.index = index;
    }

    @Override
    public String toString() {
      return '{' + name + ", " +
          desc + ", " +
          index + ", " +
          signature + '}';
    }
  }
}
