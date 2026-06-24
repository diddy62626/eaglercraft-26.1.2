/*
 *  Copyright 2016 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.model.optimization;

import org.teavm.model.*;
import org.teavm.model.instructions.*;

// PATCHED: Added null checks for getOperand(), getFirstOperand(), getSecondOperand()
// in visit(BranchingInstruction) and visit(BinaryBranchingInstruction).
// TeaVM 0.15 AGGRESSIVE optimization can create BranchingInstructions with null
// operands, causing NPE in the original code. This patch makes the analyzer
// skip null operands instead of crashing.
public final class VariableEscapeAnalyzer {
    private VariableEscapeAnalyzer() {
    }

    public static boolean[] findEscapingVariables(Program program) {
        boolean[] escaping = new boolean[program.variableCount()];
        InstructionAnalyzer analyzer = new InstructionAnalyzer(escaping);
        for (int i = 0; i < program.basicBlockCount(); ++i) {
            BasicBlock block = program.basicBlockAt(i);
            for (Instruction insn : block) {
                insn.acceptVisitor(analyzer);
            }
        }
        return escaping;
    }

    private static class InstructionAnalyzer extends AbstractInstructionVisitor {
        private boolean[] escaping;

        InstructionAnalyzer(boolean[] escaping) {
            this.escaping = escaping;
        }

        @Override
        public void visit(BranchingInstruction insn) {
            if (insn.getOperand() != null) {
                escaping[insn.getOperand().getIndex()] = true;
            }
        }

        @Override
        public void visit(BinaryBranchingInstruction insn) {
            if (insn.getFirstOperand() != null) {
                escaping[insn.getFirstOperand().getIndex()] = true;
            }
            if (insn.getSecondOperand() != null) {
                escaping[insn.getSecondOperand().getIndex()] = true;
            }
        }

        @Override
        public void visit(SwitchInstruction insn) {
            if (insn.getCondition() != null) {
                escaping[insn.getCondition().getIndex()] = true;
            }
        }

        @Override
        public void visit(ExitInstruction insn) {
            if (insn.getValueToReturn() != null) {
                escaping[insn.getValueToReturn().getIndex()] = true;
            }
        }

        @Override
        public void visit(RaiseInstruction insn) {
            if (insn.getException() != null) {
                escaping[insn.getException().getIndex()] = true;
            }
        }

        @Override
        public void visit(PutFieldInstruction insn) {
            if (insn.getInstance() != null) {
                escaping[insn.getInstance().getIndex()] = true;
            }
            if (insn.getValue() != null) {
                escaping[insn.getValue().getIndex()] = true;
            }
        }

        @Override
        public void visit(PutElementInstruction insn) {
            if (insn.getArray() != null) {
                escaping[insn.getArray().getIndex()] = true;
            }
            if (insn.getIndex() != null) {
                escaping[insn.getIndex().getIndex()] = true;
            }
            if (insn.getValue() != null) {
                escaping[insn.getValue().getIndex()] = true;
            }
        }

        @Override
        public void visit(InvokeInstruction insn) {
            if (insn.getInstance() != null) {
                escaping[insn.getInstance().getIndex()] = true;
            }
            for (Variable arg : insn.getArguments()) {
                if (arg != null) {
                    escaping[arg.getIndex()] = true;
                }
            }
        }

        @Override
        public void visit(InvokeDynamicInstruction insn) {
            if (insn.getInstance() != null) {
                escaping[insn.getInstance().getIndex()] = true;
            }
            for (Variable arg : insn.getArguments()) {
                if (arg != null) {
                    escaping[arg.getIndex()] = true;
                }
            }
        }

        @Override
        public void visit(MonitorEnterInstruction insn) {
            if (insn.getObjectRef() != null) {
                escaping[insn.getObjectRef().getIndex()] = true;
            }
        }

        @Override
        public void visit(MonitorExitInstruction insn) {
            if (insn.getObjectRef() != null) {
                escaping[insn.getObjectRef().getIndex()] = true;
            }
        }

        @Override
        public void visit(BoundCheckInstruction insn) {
            if (insn.getArray() != null) {
                escaping[insn.getArray().getIndex()] = true;
            }
            if (insn.getReceiver() != null) {
                escaping[insn.getReceiver().getIndex()] = true;
            }
        }

        @Override
        public void visit(NullCheckInstruction insn) {
            if (insn.getValue() != null) {
                escaping[insn.getValue().getIndex()] = true;
            }
            if (insn.getReceiver() != null) {
                escaping[insn.getReceiver().getIndex()] = true;
            }
        }
    }
}
