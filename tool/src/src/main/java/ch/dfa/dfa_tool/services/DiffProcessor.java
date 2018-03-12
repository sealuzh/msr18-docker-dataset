package ch.dfa.dfa_tool.services;


import ch.dfa.dfa_tool.models.Diff;
import ch.dfa.dfa_tool.models.Snapshot;
import ch.dfa.dfa_tool.models.commands.*;
import ch.dfa.dfa_tool.models.commands.enums.Instructions;
import ch.dfa.dfa_tool.models.diff.DiffType;
import ch.dfa.dfa_tool.models.diff.enums.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by salizumberi-laptop on 30.10.2016.
 */
public class DiffProcessor {
    public static Diff diff;

    public static int[] getDiffStatistic(List<DiffType> diffTypes) {
        int[] stats = new int[3];
        stats[0] = 0;
        stats[1] = 0;
        stats[2] = 0;

        if(diffTypes != null) {
            for (DiffType diffType : diffTypes) {
                if (diffType != null && diffType.getChangeType().contains("AddType")) {
                    stats[0]++;
                } else if (diffType != null && diffType.getChangeType().contains("DelType")) {
                    stats[1]++;
                } else if (diffType != null && diffType.getChangeType().contains("UpdateType")) {
                    stats[2]++;
                }
            }
        }
        return stats;
    }

    public static Diff getDiff(Snapshot oldSnapShot, Snapshot newSnapshot) {
        diff = new Diff();
        diff.setSnapshots(oldSnapShot, newSnapshot);

        if (oldSnapShot == null && newSnapshot != null) {
            diff.setDiffState("NULL_COMMIT");
            Snapshot nullSnap = new Snapshot();
            diff.diffs = getDiffTypeOfTwoSnapshot(nullSnap, newSnapshot);
            int[] stats = getDiffStatistic(diff.diffs);
            diff.setIns(stats[0]);
            diff.setDel(stats[1]);
            diff.setMod(stats[2]);
        } else if (oldSnapShot != null && newSnapshot == null) {
            diff.setDiffState("COMMIT_NULL");
            Snapshot nullSnap = new Snapshot();
            diff.diffs = getDiffTypeOfTwoSnapshot(oldSnapShot, nullSnap);
            int[] stats = getDiffStatistic(diff.diffs);
            diff.setIns(stats[0]);
            diff.setDel(stats[1]);
            diff.setMod(stats[2]);
        } else if (newSnapshot != null && oldSnapShot != null) {
            diff.setDiffState("COMMIT_COMMIT");
            Snapshot nullSnap = new Snapshot();
            diff.diffs = getDiffTypeOfTwoSnapshot(oldSnapShot, newSnapshot);
            int[] stats;
            if (diff.diffs != null) {
                stats = getDiffStatistic(diff.diffs);
            } else {
                //TODO: ENABLE COMMENTS
                stats = new int[]{0, 0, 0};
            }
            diff.setIns(stats[0]);
            diff.setDel(stats[1]);
            diff.setMod(stats[2]);

        } else {
            return null;
        }
        return diff;
    }

    public static List<DiffType> getDiffTypeOfTwoSnapshot(Snapshot oldSnapShot, Snapshot newSnapshot) {
        List<DiffType> diffTypes = new ArrayList<>();
        /*
        Single Instructions
         */
        List<DiffType> froms = getDiffOfFrom(oldSnapShot.from, newSnapshot.from);
        if (froms != null && froms.size() > 0) {
            getDiffOfFrom(oldSnapShot.from, newSnapshot.from).forEach(x -> diffTypes.add(x));
        }

        DiffType maintainer = getDiffOfMaintainer(oldSnapShot.maintainer, newSnapshot.maintainer);
        if (maintainer != null) {
            diffTypes.add(getDiffOfMaintainer(oldSnapShot.maintainer, newSnapshot.maintainer));
        }

        List<DiffType> cmds = getDiffOfCmd(oldSnapShot.cmd, newSnapshot.cmd);
        if (cmds != null) {
            getDiffOfCmd(oldSnapShot.cmd, newSnapshot.cmd).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> entryPoints = getDiffOfEntryPoint(oldSnapShot.entryPoint, newSnapshot.entryPoint);
        if (entryPoints != null && entryPoints.size() > 0) {
            getDiffOfEntryPoint(oldSnapShot.entryPoint, newSnapshot.entryPoint).forEach(x -> diffTypes.add(x));
        }

        DiffType stopSignals = getDiffOfStopSignal(oldSnapShot.stopSignals, newSnapshot.stopSignals);
        if (stopSignals != null) {
            diffTypes.add(getDiffOfStopSignal(oldSnapShot.stopSignals, newSnapshot.stopSignals));
        }


        /*
        Multiple Instructions with Params
         */

        List<DiffType> runs = getDiffOfMultipleRuns(oldSnapShot.runs, newSnapshot.runs);
        if (runs != null && runs.size() > 0) {
            getDiffOfMultipleRuns(oldSnapShot.runs, newSnapshot.runs).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> labels = getDiffOfMultipleLabels(oldSnapShot.labels, newSnapshot.labels);
        if (labels != null && labels.size() > 0) {
            getDiffOfMultipleLabels(oldSnapShot.labels, newSnapshot.labels).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> adds = getDiffOfMultipleAdds(oldSnapShot.adds, newSnapshot.adds);
        if (adds != null && adds.size() > 0) {
            getDiffOfMultipleAdds(oldSnapShot.adds, newSnapshot.adds).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> copies = getDiffOfMultipleCopies(oldSnapShot.copies, newSnapshot.copies);
        if (copies != null && copies.size() > 0) {
            getDiffOfMultipleCopies(oldSnapShot.copies, newSnapshot.copies).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> envs = getDiffOfMultipleEnvs(oldSnapShot.envs, newSnapshot.envs);
        if (envs != null && envs.size() > 0) {
            getDiffOfMultipleEnvs(oldSnapShot.envs, newSnapshot.envs).forEach(x -> diffTypes.add(x));
        }


         /*
        Multiple Instructions without Params
         */

        List<DiffType> users = getDiffOfMultipleUsers(oldSnapShot.users, newSnapshot.users);
        if (users != null && users.size() > 0) {
            getDiffOfMultipleUsers(oldSnapShot.users, newSnapshot.users).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> workDirs = getDiffOfMultipleWorkDir(oldSnapShot.workDirs, newSnapshot.workDirs);
        if (workDirs != null && workDirs.size() > 0) {
            getDiffOfMultipleWorkDir(oldSnapShot.workDirs, newSnapshot.workDirs).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> comments = getDiffOfMultipleComments(oldSnapShot.comments, newSnapshot.comments);
        if (comments != null && comments.size() > 0) {
            getDiffOfMultipleComments(oldSnapShot.comments, newSnapshot.comments).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> volumes = getDiffOfMultipleVolumes(oldSnapShot.volumes, newSnapshot.volumes);
        if (volumes != null && volumes.size() > 0) {
            getDiffOfMultipleVolumes(oldSnapShot.volumes, newSnapshot.volumes).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> exposes = getDiffOfMultipleExposes(oldSnapShot.exposes, newSnapshot.exposes);
        if (exposes != null && exposes.size() > 0) {
            getDiffOfMultipleExposes(oldSnapShot.exposes, newSnapshot.exposes).forEach(x -> diffTypes.add(x));
        }

        List<DiffType> args = getDiffOfMultipleArgs(oldSnapShot.args, newSnapshot.args);
        if (args != null && args.size() > 0) {
            getDiffOfMultipleArgs(oldSnapShot.args, newSnapshot.args).forEach(x -> diffTypes.add(x));
        }

        //getDiffOfHealthCheck(oldSnapShot.healthCheck, newSnapshot.healthCheck);
        // getDiffOfOnBuilds(oldSnapShot.onBuilds, newSnapshot.onBuilds);

        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleEnvs(List<Env> oldEnvs, List<Env> newEnvs) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Env> oldSnapShot = oldEnvs;
        List<Env> newSnapshot = newEnvs;

        /*
        Copy Arrays
         */
        List<Env> oldchanged = new ArrayList<>(oldSnapShot);
        List<Env> newchanged = new ArrayList<>(newSnapshot);

        List<Env> notChangedInstruction = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchanged.size(); x++) {
                if (oldSnapShot.get(i).key.equals(newchanged.get(x).key) &&
                        oldSnapShot.get(i).value.equals(newchanged.get(x).value)) {
                    newchanged.remove(x);
                    notChangedInstruction.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }

        }

            /*
            Delete the Runs which have not changed
             */

        List<Env> notUpdated = notChangedInstruction;
        oldchanged.removeAll(notChangedInstruction);


        List<Env> tempOld = new ArrayList<>();
        List<Env> tempNew = new ArrayList<>();
        newchanged.forEach(x -> tempNew.add(x));
        oldchanged.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            for (DiffType diffsOfNewRun : getDiffOfEnvs(tempOld.get(0), tempNew.get(0))) {
                diffTypes.add(diffsOfNewRun);
            }
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Env newInstruction : tempNew) {
                for (DiffType diffsOfNewRun : getDiffOfEnvs(null, newInstruction)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Env deletedRun : tempOld) {
                for (DiffType diffsOfNewRun : getDiffOfEnvs(deletedRun, null)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else {
            boolean mappingFlag = false;
            List<Env> deletedItems = new ArrayList<>();
            List<Env> newItems = new ArrayList<>();

            boolean found = false;
            while (!mappingFlag) {
                if (tempOld.size() != 0) {
                    for (int y = 0; y < tempOld.size(); y++) {
                        for (int x = 0; x < tempNew.size(); x++) {
                            if (tempOld.get(y).key.equals(tempNew.get(x).key)) {

                                for (DiffType diffsOfNewRun : getDiffOfEnvs(tempOld.get(y), tempNew.get(x))) {
                                    diffTypes.add(diffsOfNewRun);
                                }
                                tempOld.remove(y);
                                tempNew.remove(x);
                                found = true;
                                break;
                            }


                        }
                        if (found) {
                            found = false;
                        } else {
                            deletedItems.add(tempOld.get(y));
                            tempOld.remove(y);
                        }
                        break;
                    }
                } else {
                    for (Env newInstruction : tempNew) {
                        newItems.add(newInstruction);
                        for (DiffType diffsOfNewRun : getDiffOfEnvs(null, newInstruction)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    for (Env deletedRun : deletedItems) {
                        for (DiffType diffsOfNewRun : getDiffOfEnvs(deletedRun, null)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    mappingFlag = true;
                }
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }


    public static List<DiffType> getDiffOfMultipleCopies(List<Copy> oldAdds, List<Copy> newAdds) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Copy> oldSnapShot = oldAdds;
        List<Copy> newSnapshot = newAdds;

        /*
        Copy Arrays
         */
        List<Copy> oldchanged = new ArrayList<>(oldSnapShot);
        List<Copy> newchanged = new ArrayList<>(newSnapshot);

        List<Copy> notChangedInstruction = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchanged.size(); x++) {
                if (oldSnapShot.get(i).source.equals(newchanged.get(x).source) &&
                        oldSnapShot.get(i).destination.equals(newchanged.get(x).destination)) {
                    newchanged.remove(x);
                    notChangedInstruction.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }

        }

            /*
            Delete the Runs which have not changed
             */

        List<Copy> notUpdated = notChangedInstruction;
        oldchanged.removeAll(notChangedInstruction);


        List<Copy> tempOld = new ArrayList<>();
        List<Copy> tempNew = new ArrayList<>();
        newchanged.forEach(x -> tempNew.add(x));
        oldchanged.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            for (DiffType diffsOfNewRun : getDiffOfCopies(tempOld.get(0), tempNew.get(0))) {
                diffTypes.add(diffsOfNewRun);
            }
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Copy newInstruction : tempNew) {
                for (DiffType diffsOfNewRun : getDiffOfCopies(null, newInstruction)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Copy deletedRun : tempOld) {
                for (DiffType diffsOfNewRun : getDiffOfCopies(deletedRun, null)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else {
            boolean mappingFlag = false;
            List<Copy> deletedItems = new ArrayList<>();
            List<Copy> newItems = new ArrayList<>();

            boolean found = false;
            while (!mappingFlag) {
                if (tempOld.size() != 0) {
                    for (int y = 0; y < tempOld.size(); y++) {
                        for (int x = 0; x < tempNew.size(); x++) {
                            if (tempOld.get(y).source.equals(tempNew.get(x).source)) {
                                for (DiffType diffsOfNewRun : getDiffOfCopies(tempOld.get(y), tempNew.get(x))) {
                                    diffTypes.add(diffsOfNewRun);
                                }
                                tempOld.remove(y);
                                tempNew.remove(x);
                                found = true;
                                break;
                            }


                        }
                        if (found) {
                            found = false;
                        } else {
                            deletedItems.add(tempOld.get(y));
                            tempOld.remove(y);
                        }
                        break;
                    }
                } else {
                    for (Copy newInstruction : tempNew) {
                        newItems.add(newInstruction);
                        for (DiffType diffsOfNewRun : getDiffOfCopies(null, newInstruction)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    for (Copy deletedRun : deletedItems) {
                        for (DiffType diffsOfNewRun : getDiffOfCopies(deletedRun, null)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    mappingFlag = true;
                }
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleAdds(List<Add> oldAdds, List<Add> newAdds) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Add> oldSnapShot = oldAdds;
        List<Add> newSnapshot = newAdds;

        /*
        Copy Arrays
         */
        List<Add> oldchanged = new ArrayList<>(oldSnapShot);
        List<Add> newchanged = new ArrayList<>(newSnapshot);

        List<Add> notChangedInstruction = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchanged.size(); x++) {
                if (oldSnapShot.get(i).source.equals(newchanged.get(x).source) &&
                        oldSnapShot.get(i).destination.equals(newchanged.get(x).destination)) {
                    newchanged.remove(x);
                    notChangedInstruction.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }

        }

            /*
            Delete the Runs which have not changed
             */

        List<Add> notUpdated = notChangedInstruction;
        oldchanged.removeAll(notChangedInstruction);


        List<Add> tempOld = new ArrayList<>();
        List<Add> tempNew = new ArrayList<>();
        newchanged.forEach(x -> tempNew.add(x));
        oldchanged.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            for (DiffType diffsOfNewRun : getDiffOfAdds(tempOld.get(0), tempNew.get(0))) {
                diffTypes.add(diffsOfNewRun);
            }
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Add newInstruction : tempNew) {
                for (DiffType diffsOfNewRun : getDiffOfAdds(null, newInstruction)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Add deletedRun : tempOld) {
                for (DiffType diffsOfNewRun : getDiffOfAdds(deletedRun, null)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else {
            boolean mappingFlag = false;
            List<Add> deletedItems = new ArrayList<>();
            List<Add> newItems = new ArrayList<>();

            boolean found = false;
            while (!mappingFlag) {
                if (tempOld.size() != 0) {
                    for (int y = 0; y < tempOld.size(); y++) {
                        for (int x = 0; x < tempNew.size(); x++) {
                            if (tempOld.get(y).source.equals(tempNew.get(x).source)) {
                                for (DiffType diffsOfNewRun : getDiffOfAdds(tempOld.get(y), tempNew.get(x))) {
                                    diffTypes.add(diffsOfNewRun);
                                }
                                tempOld.remove(y);
                                tempNew.remove(x);
                                found = true;
                                break;
                            }


                        }
                        if (found) {
                            found = false;
                        } else {
                            deletedItems.add(tempOld.get(y));
                            tempOld.remove(y);
                        }
                        break;
                    }
                } else {
                    for (Add newInstruction : tempNew) {
                        newItems.add(newInstruction);
                        for (DiffType diffsOfNewRun : getDiffOfAdds(null, newInstruction)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    for (Add deletedRun : deletedItems) {
                        for (DiffType diffsOfNewRun : getDiffOfAdds(deletedRun, null)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    mappingFlag = true;
                }
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }


    public static List<DiffType> getDiffOfMultipleLabels(List<Label> oldLabels, List<Label> newLabels) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Label> oldSnapShot = oldLabels;
        List<Label> newSnapshot = newLabels;

        /*
        Copy Arrays
         */
        List<Label> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Label> newchangedRuns = new ArrayList<>(newSnapshot);

        List<Label> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).key.equals(newchangedRuns.get(x).key) &&
                        oldSnapShot.get(i).value.equals(newchangedRuns.get(x).value)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }

        }

            /*
            Delete the Runs which have not changed
             */

        List<Label> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);


        List<Label> tempOldRunList = new ArrayList<>();
        List<Label> tempNewRunList = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNewRunList.add(x));
        oldchangedRuns.forEach(x -> tempOldRunList.add(x));

        if (tempOldRunList.size() == 1 & tempNewRunList.size() == 1) {
            for (DiffType diffsOfNewRun : getDiffOfLabels(tempOldRunList.get(0), tempNewRunList.get(0))) {
                diffTypes.add(diffsOfNewRun);
            }
        } else if (tempOldRunList.size() == 0 & tempNewRunList.size() > 0) {
            for (Label newRun : tempNewRunList) {
                for (DiffType diffsOfNewRun : getDiffOfLabels(null, newRun)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else if (tempOldRunList.size() > 0 & tempNewRunList.size() == 0) {
            for (Label deletedRun : tempOldRunList) {
                for (DiffType diffsOfNewRun : getDiffOfLabels(deletedRun, null)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else {
            boolean mappingFlag = false;
            List<Label> deletedRuns = new ArrayList<>();
            List<Label> newRuns = new ArrayList<>();

            boolean found = false;
            while (!mappingFlag) {
                if (tempOldRunList.size() != 0) {
                    for (int y = 0; y < tempOldRunList.size(); y++) {
                        for (int x = 0; x < tempNewRunList.size(); x++) {
                            if (tempOldRunList.get(y).key.equals(tempNewRunList.get(x).key)) {
                                for (DiffType diffsOfNewRun : getDiffOfLabels(tempOldRunList.get(y), tempNewRunList.get(x))) {
                                    diffTypes.add(diffsOfNewRun);
                                }
                                tempOldRunList.remove(y);
                                tempNewRunList.remove(x);
                                found = true;
                                break;
                            }


                        }
                        if (found) {
                            found = false;
                        } else {
                            deletedRuns.add(tempOldRunList.get(y));
                            tempOldRunList.remove(y);
                        }
                        break;
                    }
                } else {
                    for (Label newRun : tempNewRunList) {
                        newRuns.add(newRun);
                        for (DiffType diffsOfNewRun : getDiffOfLabels(null, newRun)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    for (Label deletedRun : deletedRuns) {
                        for (DiffType diffsOfNewRun : getDiffOfLabels(deletedRun, null)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    mappingFlag = true;
                }
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleRuns(List<Run> oldListRuns, List<Run> newListRuns) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Run> oldSnapShot = oldListRuns;
        List<Run> newSnapshot = newListRuns;
        /*
        Copy Arrays
         */
        List<Run> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Run> newchangedRuns = new ArrayList<>(newSnapshot);

        List<Run> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                String oldExec = oldSnapShot.get(i).executable;
                String oldParams = oldSnapShot.get(i).allParams;
                String newExec = newSnapshot.get(x).executable;
                String newParams = newSnapshot.get(x).allParams;

                if (oldSnapShot.get(i).executable.equals(newchangedRuns.get(x).executable) &&
                        oldSnapShot.get(i).allParams.equals(newchangedRuns.get(x).allParams)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }

            /*
            Delete the Runs which have not changed
             */

        List<Run> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);


        List<Run> tempOldRunList = new ArrayList<>();
        List<Run> tempNewRunList = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNewRunList.add(x));
        oldchangedRuns.forEach(x -> tempOldRunList.add(x));

        if (tempOldRunList.size() == 1 & tempNewRunList.size() == 1) {
            for (DiffType diffsOfNewRun : getDiffsOfRun(tempOldRunList.get(0), tempNewRunList.get(0))) {
                diffTypes.add(diffsOfNewRun);
            }
        } else if (tempOldRunList.size() == 0 & tempNewRunList.size() > 0) {
            for (Run newRun : tempNewRunList) {
                for (DiffType diffsOfNewRun : getDiffsOfRun(null, newRun)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else if (tempOldRunList.size() > 0 & tempNewRunList.size() == 0) {
            for (Run deletedRun : tempOldRunList) {
                for (DiffType diffsOfNewRun : getDiffsOfRun(deletedRun, null)) {
                    diffTypes.add(diffsOfNewRun);
                }
            }
        } else {
            boolean mappingFlag = false;
            List<Run> possibleMappingList = new ArrayList<>();
            List<Run> deletedRuns = new ArrayList<>();
            List<Run> newRuns = new ArrayList<>();
            List<Run> updated = new ArrayList<>();
            while (!mappingFlag) {
                if (tempOldRunList.size() != 0) {
                    for (int y = 0; y < tempOldRunList.size(); y++) {
                        for (int x = 0; x < tempNewRunList.size(); x++) {
                            if (tempOldRunList.get(y).executable.equals(tempNewRunList.get(x).executable)) {
                                possibleMappingList.add(tempNewRunList.get(x));
                            }
                        }
                        if (possibleMappingList.size() > 0) {
                            for (int i = 0; i < possibleMappingList.size(); i++) {
                                possibleMappingList.get(i).score = getParamterScore(tempOldRunList.get(y).params, possibleMappingList.get(i).params);
                            }

                            Integer indexOfMaxRun = null;
                            int maxScore = 0;
                            for (int i = 0; i < possibleMappingList.size(); i++) {
                                if (possibleMappingList.get(i).score > maxScore) {
                                    indexOfMaxRun = i;
                                    maxScore = possibleMappingList.get(i).score;
                                }
                            }

                            if (indexOfMaxRun != null) {
                                updated.add(tempOldRunList.get(y));
                                updated.add(possibleMappingList.get(indexOfMaxRun));
                                for (DiffType diffsOfNewRun : getDiffsOfRun(tempOldRunList.get(y), possibleMappingList.get(indexOfMaxRun))) {
                                    diffTypes.add(diffsOfNewRun);
                                }
                            }
                            tempOldRunList.remove(y);
                            Run mappedRun = possibleMappingList.get(indexOfMaxRun);
                            for (int i = 0; i < tempNewRunList.size(); i++) {
                                if (tempNewRunList.get(i).executable.equals(mappedRun.executable) &&
                                        tempNewRunList.get(i).allParams.equals(mappedRun.allParams)) {
                                    tempNewRunList.remove(i);
                                    break;
                                }
                            }
                            possibleMappingList = new ArrayList<>();
                        } else {
                            deletedRuns.add(tempOldRunList.get(y));
                            tempOldRunList.remove(y);
                        }
                    }
                } else {
                    for (Run newRun : tempNewRunList) {
                        newRuns.add(newRun);
                        for (DiffType diffsOfNewRun : getDiffsOfRun(null, newRun)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    for (Run deletedRun : deletedRuns) {
                        for (DiffType diffsOfNewRun : getDiffsOfRun(deletedRun, null)) {
                            diffTypes.add(diffsOfNewRun);
                        }
                    }
                    mappingFlag = true;
                    possibleMappingList = new ArrayList<>();
                    //   deletedRuns = new ArrayList<>();
                    //   newRuns = new ArrayList<>();
                }
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }

    }

    public static List<DiffType> getDiffOfMultipleUsers(List<User> oldUsers, List<User> newUsers) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<User> oldSnapShot = oldUsers;
        List<User> newSnapshot = newUsers;
        /*
        Copy Arrays
         */
        List<User> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<User> newchangedRuns = new ArrayList<>(newSnapshot);
        List<User> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).username.equals(newchangedRuns.get(x).username)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }

            /*
            Delete the Runs which have not changed
             */

        List<User> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);


        List<User> tempOld = new ArrayList<>();
        List<User> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfUsers(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (User newOnes : tempNew) {
                DiffType diffType = getDiffOfUsers(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (User deletedOnes : tempOld) {
                DiffType diffType = getDiffOfUsers(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else {
            for (User deletedOnes : tempOld) {
                DiffType diffType = getDiffOfUsers(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (User newOnes : tempNew) {
                DiffType diffType = getDiffOfUsers(null, newOnes);
                diffTypes.add(diffType);
            }
        }


        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleWorkDir(List<WorkDir> oldWorkdirs, List<WorkDir> newWorkDirs) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<WorkDir> oldSnapShot = oldWorkdirs;
        List<WorkDir> newSnapshot = newWorkDirs;
        /*
        Copy Arrays
         */
        List<WorkDir> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<WorkDir> newchangedRuns = new ArrayList<>(newSnapshot);
        List<WorkDir> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).path.equals(newchangedRuns.get(x).path)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }
        List<WorkDir> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);

        List<WorkDir> tempOld = new ArrayList<>();
        List<WorkDir> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfWorkDirs(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (WorkDir newOnes : tempNew) {
                DiffType diffType = getDiffOfWorkDirs(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (WorkDir deletedOnes : tempOld) {
                DiffType diffType = getDiffOfWorkDirs(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else {
            for (WorkDir deletedOnes : tempOld) {
                DiffType diffType = getDiffOfWorkDirs(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (WorkDir newOnes : tempNew) {
                DiffType diffType = getDiffOfWorkDirs(null, newOnes);
                diffTypes.add(diffType);
            }
        }


        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleComments(List<Comment> oldWorkdirs, List<Comment> newWorkDirs) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Comment> oldSnapShot = oldWorkdirs;
        List<Comment> newSnapshot = newWorkDirs;
        /*
        Copy Arrays
         */
        List<Comment> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Comment> newchangedRuns = new ArrayList<>(newSnapshot);
        List<Comment> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).comment.equals(newchangedRuns.get(x).comment)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }
        List<Comment> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);

        List<Comment> tempOld = new ArrayList<>();
        List<Comment> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfComments(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Comment newOnes : tempNew) {
                DiffType diffType = getDiffOfComments(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Comment deletedOnes : tempOld) {
                DiffType diffType = getDiffOfComments(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 1 & tempNew.size() > 1) {
            int bestIndex = -1;
            boolean allTested = false;
            while (!allTested) {
                epoche:
                for (int i = 0; i < tempOld.size(); i++) {
                    if (i == tempOld.size() - 1) {
                        allTested = true;
                    }

                    int max = 0;
                    for (int x = 0; x < tempNew.size(); x++) {
                        Splitter splitter = Splitter.onPattern("\\W").trimResults().omitEmptyStrings();
                        Set<String> intersection = Sets.intersection(//
                                Sets.newHashSet(splitter.split(tempOld.get(i).comment)), //
                                Sets.newHashSet(splitter.split(tempNew.get(x).comment)));

                        if (intersection.size() > max) {
                            max = intersection.size();
                            bestIndex = x;
                        }

                    }
                    if (max > 0 && tempOld.get(i).instructionAfter.equals(tempNew.get(bestIndex).instructionAfter)) {
                        DiffType diffType = getDiffOfComments(tempOld.get(i), tempNew.get(bestIndex));
                        diffTypes.add(diffType);
                        tempNew.remove(bestIndex);
                        tempOld.remove(i);
                        break epoche;
                    } else {

                    }
                }
            }
            for (Comment deletedOnes : tempOld) {
                DiffType diffType = getDiffOfComments(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (Comment newOnes : tempNew) {
                DiffType diffType = getDiffOfComments(null, newOnes);
                diffTypes.add(diffType);
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleVolumes(List<Volume> oldVolumes, List<Volume> newVolumes) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Volume> oldSnapShot = oldVolumes;
        List<Volume> newSnapshot = newVolumes;
        /*
        Copy Arrays
         */
        List<Volume> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Volume> newchangedRuns = new ArrayList<>(newSnapshot);
        List<Volume> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).value.equals(newchangedRuns.get(x).value)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }
        List<Volume> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);

        List<Volume> tempOld = new ArrayList<>();
        List<Volume> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfVolumes(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Volume newOnes : tempNew) {
                DiffType diffType = getDiffOfVolumes(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Volume deletedOnes : tempOld) {
                DiffType diffType = getDiffOfVolumes(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else {
            for (Volume deletedOnes : tempOld) {
                DiffType diffType = getDiffOfVolumes(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (Volume newOnes : tempNew) {
                DiffType diffType = getDiffOfVolumes(null, newOnes);
                diffTypes.add(diffType);
            }
        }


        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleArgs(List<Arg> oldArg, List<Arg> newArg) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Arg> oldSnapShot = oldArg;
        List<Arg> newSnapshot = newArg;
        /*
        Copy Arrays
         */
        List<Arg> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Arg> newchangedRuns = new ArrayList<>(newSnapshot);
        List<Arg> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {
                if (oldSnapShot.get(i).arg.equals(newchangedRuns.get(x).arg)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }
        List<Arg> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);

        List<Arg> tempOld = new ArrayList<>();
        List<Arg> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfArgs(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Arg newOnes : tempNew) {
                DiffType diffType = getDiffOfArgs(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Arg deletedOnes : tempOld) {
                DiffType diffType = getDiffOfArgs(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else {
            for (Arg deletedOnes : tempOld) {
                DiffType diffType = getDiffOfArgs(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (Arg newOnes : tempNew) {
                DiffType diffType = getDiffOfArgs(null, newOnes);
                diffTypes.add(diffType);
            }
        }

        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }

    public static List<DiffType> getDiffOfMultipleExposes(List<Expose> oldExposes, List<Expose> newExposes) {
        List<DiffType> diffTypes = new ArrayList<>();
        List<Expose> oldSnapShot = oldExposes;
        List<Expose> newSnapshot = newExposes;

        /*
        Copy Arrays
         */
        List<Expose> oldchangedRuns = new ArrayList<>(oldSnapShot);
        List<Expose> newchangedRuns = new ArrayList<>(newSnapshot);

        List<Expose> notChangedRuns = new ArrayList<>();

        //CLEAN 1: Remove equal Run Instructions

        for (int i = 0; i < oldSnapShot.size(); i++) {
            boolean foundFlag = false;
            innerloop:
            for (int x = 0; x < newchangedRuns.size(); x++) {

                if (oldSnapShot.get(i).port == (newchangedRuns.get(x).port)) {
                    newchangedRuns.remove(x);
                    notChangedRuns.add(oldSnapShot.get(i));
                    break innerloop;
                }
            }
        }

        List<Expose> notUpdated = notChangedRuns;
        oldchangedRuns.removeAll(notChangedRuns);

        List<Expose> tempOld = new ArrayList<>();
        List<Expose> tempNew = new ArrayList<>();
        newchangedRuns.forEach(x -> tempNew.add(x));
        oldchangedRuns.forEach(x -> tempOld.add(x));

        if (tempOld.size() == 1 & tempNew.size() == 1) {
            DiffType diffType = getDiffOfExposes(tempOld.get(0), tempNew.get(0));
            diffTypes.add(diffType);
        } else if (tempOld.size() == 0 & tempNew.size() > 0) {
            for (Expose newOnes : tempNew) {
                DiffType diffType = getDiffOfExposes(null, newOnes);
                diffTypes.add(diffType);
            }
        } else if (tempOld.size() > 0 & tempNew.size() == 0) {
            for (Expose deletedOnes : tempOld) {
                DiffType diffType = getDiffOfExposes(deletedOnes, null);
                diffTypes.add(diffType);
            }
        } else {
            for (Expose deletedOnes : tempOld) {
                DiffType diffType = getDiffOfExposes(deletedOnes, null);
                diffTypes.add(diffType);
            }
            for (Expose newOnes : tempNew) {
                DiffType diffType = getDiffOfExposes(null, newOnes);
                diffTypes.add(diffType);
            }
        }
        if (diffTypes.size() > 0) {
            return diffTypes;
        } else {
            return null;
        }
    }


    public static int getParamterScore(List<String> oldParams, List<String> newParams) {
        List<String> oldP = new ArrayList<>();
        List<String> newP = new ArrayList<>();

        oldParams.forEach(x -> oldP.add(x));
        newParams.forEach(x -> newP.add(x));
        oldP.retainAll(newP);
        return oldP.size() + 1;
    }

    public static DiffType getDiffOfWorkDirs(WorkDir oldWorkDir, WorkDir newWorkDir) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.WORKDIR);
        if (oldWorkDir == null && newWorkDir != null) {
            diffType.addChangeType(AddType.WORKDIR);
            diffType.setBeforeAndAfter(null, newWorkDir.path);
        } else if (oldWorkDir != null && newWorkDir == null) {
            diffType.addChangeType(DelType.WORKDIR);
            diffType.setBeforeAndAfter(oldWorkDir.path, null);
        } else if (oldWorkDir != null && newWorkDir != null) {
            if (!oldWorkDir.path.equals(newWorkDir.path)) {
                diffType.addChangeType(UpdateType.PATH);
                diffType.setBeforeAndAfter(oldWorkDir.path, newWorkDir.path);
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static DiffType getDiffOfComments(Comment oldComment, Comment newComment) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.COMMENT);
        if (oldComment == null && newComment != null) {
            diffType.addChangeType(AddType.COMMENT);
            diffType.setBeforeAndAfter(null, newComment.comment);
        } else if (oldComment != null && newComment == null) {
            diffType.addChangeType(DelType.COMMENT);
            diffType.setBeforeAndAfter(oldComment.comment, null);
        } else if (oldComment != null && newComment != null) {
            if (!oldComment.comment.equals(newComment.comment)) {
                diffType.addChangeType(UpdateType.COMMENT);
                diffType.setBeforeAndAfter(oldComment.comment, newComment.comment);
            }else{
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static DiffType getDiffOfUsers(User oldUser, User newUser) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.USER);
        if (oldUser == null && newUser != null) {
            diffType.addChangeType(AddType.USER);
            diffType.setBeforeAndAfter(null, newUser.username);
        } else if (oldUser != null && newUser == null) {
            diffType.addChangeType(DelType.USER);
            diffType.setBeforeAndAfter(oldUser.username, null);
        } else if (oldUser != null && newUser != null) {
            if (!oldUser.username.equals(newUser.username)) {
                diffType.addChangeType(UpdateType.USER_NAME);
                diffType.setBeforeAndAfter(oldUser.username, newUser.username);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static DiffType getDiffOfVolumes(Volume oldMaintainer, Volume newVolume) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.VOLUME);
        if (oldMaintainer == null && newVolume != null) {
            diffType.addChangeType(AddType.VOLUME);
            diffType.setBeforeAndAfter(null, newVolume.value);
        } else if (oldMaintainer != null && newVolume == null) {
            diffType.addChangeType(DelType.VOLUME);
            diffType.setBeforeAndAfter(oldMaintainer.value, null);
        } else if (oldMaintainer != null && newVolume != null) {
            if (!oldMaintainer.value.equals(newVolume.value)) {
                diffType.addChangeType(UpdateType.VALUE);
                diffType.setBeforeAndAfter(oldMaintainer.value, newVolume.value);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static List<DiffType> getDiffOfCopies(Copy oldCopie, Copy newCopie) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldCopie == null && newCopie != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.COPY);
            diffType.addChangeType(AddType.COPY);
            diffType.setBeforeAndAfter(null, newCopie.sourceDestination);
            diffTypes.add(diffType);
        } else if (oldCopie != null && newCopie == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.COPY);
            diffType.addChangeType(DelType.COPY);
            diffType.setBeforeAndAfter(oldCopie.sourceDestination, null);
            diffTypes.add(diffType);
        } else if (oldCopie != null && newCopie != null) {
            if (!oldCopie.sourceDestination.equals(newCopie.sourceDestination)) {
                if (oldCopie.source.equals(newCopie.source)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.COPY);
                    diffType.addChangeType(UpdateType.DESTINATION);
                    diffType.setBeforeAndAfter(oldCopie.sourceDestination, newCopie.sourceDestination);
                    diffTypes.add(diffType);
                }
                if (oldCopie.destination.equals(newCopie.destination)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.COPY);
                    diffType.addChangeType(UpdateType.SOURCE);
                    diffType.setBeforeAndAfter(oldCopie.sourceDestination, newCopie.sourceDestination);
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static List<DiffType> getDiffOfAdds(Add oldAdd, Add newAdd) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldAdd == null && newAdd != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ADD);
            diffType.addChangeType(AddType.ADD);
            diffType.setBeforeAndAfter(null, newAdd.sourceDestination);
            diffTypes.add(diffType);
        } else if (oldAdd != null && newAdd == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ADD);
            diffType.addChangeType(DelType.ADD);
            diffType.setBeforeAndAfter(oldAdd.sourceDestination, null);
            diffTypes.add(diffType);
        } else if (oldAdd != null && newAdd != null) {
            if (!oldAdd.sourceDestination.equals(newAdd.sourceDestination)) {
                if (oldAdd.source.equals(newAdd.source)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.ADD);
                    diffType.addChangeType(UpdateType.DESTINATION);
                    diffType.setBeforeAndAfter(oldAdd.sourceDestination, newAdd.sourceDestination);
                    diffTypes.add(diffType);
                }
                if (oldAdd.destination.equals(newAdd.destination)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.ADD);
                    diffType.addChangeType(UpdateType.SOURCE);
                    diffType.setBeforeAndAfter(oldAdd.sourceDestination, newAdd.sourceDestination);
                    diffTypes.add(diffType);
                }
            }
        }
            return diffTypes;
    }

    public static DiffType getDiffOfExposes(Expose oldExpose, Expose newExpose) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.EXPOSE);
        if (oldExpose == null && newExpose != null) {
            diffType.addChangeType(AddType.EXPOSE);
            diffType.setBeforeAndAfter(null, String.valueOf(newExpose.port));
        } else if (oldExpose != null && newExpose == null) {
            diffType.addChangeType(DelType.EXPOSE);
            diffType.setBeforeAndAfter(String.valueOf(oldExpose.port), null);
        } else if (oldExpose != null && newExpose != null) {
            if (oldExpose.port != (newExpose.port)) {
                diffType.addChangeType(UpdateType.PORT);
                diffType.setBeforeAndAfter(String.valueOf(oldExpose.port), String.valueOf(newExpose.port));
            } else {
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static List<DiffType> getDiffOfEnvs(Env oldEnv, Env newEnv) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldEnv == null && newEnv != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ENV);
            diffType.addChangeType(AddType.ENV);
            diffType.setBeforeAndAfter(null, newEnv.keyValue);
            diffTypes.add(diffType);
        } else if (oldEnv != null && newEnv == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ENV);
            diffType.addChangeType(DelType.ENV);
            diffType.setBeforeAndAfter(oldEnv.keyValue, null);
            diffTypes.add(diffType);
        } else if (oldEnv != null && newEnv != null) {
            if (!oldEnv.keyValue.equals(newEnv.keyValue)) {
                if (oldEnv.key.equals(newEnv.key)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.ENV);
                    diffType.addChangeType(UpdateType.VALUE);
                    diffType.setBeforeAndAfter(oldEnv.keyValue, newEnv.keyValue);
                    diffTypes.add(diffType);
                }
                if (oldEnv.value.equals(newEnv.value)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.ENV);
                    diffType.addChangeType(UpdateType.KEY);
                    diffType.setBeforeAndAfter(oldEnv.keyValue, newEnv.keyValue);
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static List<DiffType> getDiffOfLabels(Label oldLabel, Label newLabel) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldLabel == null && newLabel != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.LABEL);
            diffType.addChangeType(AddType.LABEL);
            diffType.setBeforeAndAfter(null, newLabel.keyValue);
            diffTypes.add(diffType);
        } else if (oldLabel != null && newLabel == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.LABEL);
            diffType.addChangeType(DelType.LABEL);
            diffType.setBeforeAndAfter(oldLabel.keyValue, null);
            diffTypes.add(diffType);
        } else if (oldLabel != null && newLabel != null) {
            if (!oldLabel.keyValue.equals(newLabel.keyValue)) {
                if (oldLabel.key.equals(newLabel.key)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.LABEL);
                    diffType.addChangeType(UpdateType.VALUE);
                    diffType.setBeforeAndAfter(oldLabel.keyValue, newLabel.keyValue);
                    diffTypes.add(diffType);
                }
                if (oldLabel.value.equals(newLabel.value)) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(Instructions.LABEL);
                    diffType.addChangeType(UpdateType.KEY);
                    diffType.setBeforeAndAfter(oldLabel.keyValue, newLabel.keyValue);
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static DiffType getDiffOfArgs(Arg oldArg, Arg newArg) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.ARG);
        if (oldArg == null && newArg != null) {
            diffType.addChangeType(AddType.ARG);
            diffType.setBeforeAndAfter(null, newArg.arg);
        } else if (oldArg != null && newArg == null) {
            diffType.addChangeType(DelType.ARG);
            diffType.setBeforeAndAfter(oldArg.arg, null);
        } else if (oldArg != null && newArg != null) {
            if (!oldArg.arg.equals(newArg.arg)) {
                diffType.addChangeType(UpdateType.ARG);
                diffType.setBeforeAndAfter(oldArg.arg, newArg.arg);
            }
        } else {
            return null;
        }
        return diffType;
    }

    //TODO: ONBUILD!
    public static DiffType getDiffOfOnBuilds(List<OnBuild> oldOnBuilds, List<OnBuild> newOnBuilds) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.ONBUILD);
        return diffType;
    }

    public static List<DiffType> getDiffsOfRun(Run oldRun, Run newRun) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldRun == null && newRun != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.RUN);
            diffType.addChangeType(AddType.RUN);
            diffType.setBeforeAndAfter(null, newRun.allParams, newRun.executable);
            diffTypes.add(diffType);
        } else if (oldRun != null && newRun == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.RUN);
            diffType.addChangeType(DelType.RUN);
            diffType.setBeforeAndAfter(oldRun.allParams, null, oldRun.executable);
            diffTypes.add(diffType);
        } else if (oldRun != null && newRun != null) {
            if (!oldRun.executable.equals(newRun.executable) && oldRun.allParams.equals(newRun.allParams)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.RUN);
                diffType.addChangeType(UpdateType.EXECUTABLE);
                diffType.setBeforeAndAfter(oldRun.executable, newRun.executable);
                diffTypes.add(diffType);
            } else if (oldRun.executable.equals(newRun.executable) && !oldRun.allParams.equals(newRun.allParams)) {
                for (DiffType diffType : getParamChanges(oldRun.params, newRun.params, oldRun.executable, Instructions.RUN, false)) {
                    diffTypes.add(diffType);
                }
            } else if (!oldRun.executable.equals(newRun.executable) && !oldRun.allParams.equals(newRun.allParams)) {
                DiffType diffTypeE = new DiffType(diff);
                diffTypeE.setInstruction(Instructions.RUN);
                diffTypeE.addChangeType(UpdateType.EXECUTABLE_PARAMETER);
                diffTypeE.setBeforeAndAfter(oldRun.executable, newRun.executable);
                diffTypes.add(diffTypeE);

                for (DiffType diffType : getParamChanges(oldRun.params, newRun.params, oldRun.executable, Instructions.RUN, true)) {
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }


    public static DiffType getDiffOfStopSignal(StopSignal oldStopSignals, StopSignal newStopSignals) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.STOPSIGNAL);
        if (oldStopSignals == null && newStopSignals != null) {
            diffType.addChangeType(AddType.STOPSIGNAL);
            diffType.setBeforeAndAfter(null, newStopSignals.signal);
        } else if (oldStopSignals != null && newStopSignals == null) {
            diffType.addChangeType(DelType.STOPSIGNAL);
            diffType.setBeforeAndAfter(oldStopSignals.signal, null);
        } else if (oldStopSignals != null && newStopSignals != null) {
            if (!oldStopSignals.signal.equals(newStopSignals.signal)) {
                diffType.addChangeType(UpdateType.STOPSIGNAL);
                diffType.setBeforeAndAfter(oldStopSignals.signal, newStopSignals.signal);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static List<DiffType> getDiffOfEntryPoint(EntryPoint oldEntryPoint, EntryPoint newEntryPoint) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldEntryPoint == null && newEntryPoint != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ENTRYPOINT);
            diffType.addChangeType(AddType.ENTRYPOINT);
            diffType.setBeforeAndAfter(null, newEntryPoint.allParams, newEntryPoint.executable);
            diffTypes.add(diffType);
        } else if (oldEntryPoint != null && newEntryPoint == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.ENTRYPOINT);
            diffType.addChangeType(DelType.ENTRYPOINT);
            diffType.setBeforeAndAfter(oldEntryPoint.allParams, null, oldEntryPoint.executable);
            diffTypes.add(diffType);
        } else if (oldEntryPoint != null && newEntryPoint != null) {
            if (!oldEntryPoint.executable.equals(newEntryPoint.executable) && oldEntryPoint.allParams.equals(newEntryPoint.allParams)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.ENTRYPOINT);
                diffType.addChangeType(UpdateType.EXECUTABLE);
                diffType.setBeforeAndAfter(oldEntryPoint.executable, newEntryPoint.executable);
                diffTypes.add(diffType);
            } else if (oldEntryPoint.executable.equals(newEntryPoint.executable) && !oldEntryPoint.allParams.equals(newEntryPoint.allParams)) {
                for (DiffType diffType : getParamChanges(oldEntryPoint.params, newEntryPoint.params, oldEntryPoint.executable, Instructions.ENTRYPOINT, false)) {
                    diffTypes.add(diffType);
                }
            } else if (!oldEntryPoint.executable.equals(newEntryPoint.executable) && !oldEntryPoint.allParams.equals(newEntryPoint.allParams)) {
                DiffType diffTypeE = new DiffType(diff);
                diffTypeE.setInstruction(Instructions.ENTRYPOINT);
                diffTypeE.addChangeType(UpdateType.EXECUTABLE_PARAMETER);
                diffTypeE.setBeforeAndAfter(oldEntryPoint.executable, newEntryPoint.executable);
                diffTypes.add(diffTypeE);

                for (DiffType diffType : getParamChanges(oldEntryPoint.params, newEntryPoint.params, oldEntryPoint.executable, Instructions.ENTRYPOINT, true)) {
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static List<DiffType> getDiffOfCmd(Cmd oldCmd, Cmd newCmd) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldCmd == null && newCmd != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.CMD);
            diffType.addChangeType(AddType.CMD);
            diffType.setBeforeAndAfter(null, newCmd.allParams, newCmd.executable);
            diffTypes.add(diffType);
        } else if (oldCmd != null && newCmd == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.CMD);
            diffType.addChangeType(DelType.CMD);
            diffType.setBeforeAndAfter(oldCmd.allParams, null, oldCmd.executable);
            diffTypes.add(diffType);
        } else if (oldCmd != null && newCmd != null) {
            if (!oldCmd.executable.equals(newCmd.executable) && oldCmd.allParams.equals(newCmd.allParams)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.CMD);
                diffType.addChangeType(UpdateType.CMD);
                diffType.setBeforeAndAfter(oldCmd.executable, newCmd.executable);
                diffTypes.add(diffType);
            } else if (oldCmd.executable.equals(newCmd.executable) && !oldCmd.allParams.equals(newCmd.allParams)) {
                for (DiffType diffType : getParamChanges(oldCmd.params, newCmd.params, oldCmd.executable, Instructions.CMD, false)) {
                    diffTypes.add(diffType);
                }
            } else if (!oldCmd.executable.equals(newCmd.executable) && !oldCmd.allParams.equals(newCmd.allParams)) {
                DiffType diffTypeE = new DiffType(diff);
                diffTypeE.setInstruction(Instructions.CMD);
                diffTypeE.addChangeType(UpdateType.EXECUTABLE_PARAMETER);
                diffTypeE.setBeforeAndAfter(oldCmd.executable, newCmd.executable);
                diffTypes.add(diffTypeE);

                for (DiffType diffType : getParamChanges(oldCmd.params, newCmd.params, oldCmd.executable, Instructions.CMD, true)) {
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static List<DiffType> getParamChanges(List<String> oldParams, List<String> newParams, String executable, Instructions instructionType, boolean bothChanged) {
        List<DiffType> diffTypes = new ArrayList<>();

        List<String> oldP = new ArrayList<>(oldParams);
        List<String> newP = new ArrayList<>(newParams);

        List<String> paramsTempa = new ArrayList<>(oldParams);
        List<String> paramsTempb = new ArrayList<>(newParams);
        paramsTempa.retainAll(paramsTempb);
        paramsTempb.retainAll(oldParams);

        List<String> intersectiona = new ArrayList<>(paramsTempa);
        List<String> intersectionb = new ArrayList<>(paramsTempb);

        oldP.removeAll(intersectiona);
        newP.removeAll(intersectionb);

        if (oldP.size() == 1 && newP.size() == 1) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(instructionType);
            if (bothChanged) {
                diffType.addChangeType(UpdateType.EXECUTABLE_PARAMETER);
            } else {
                diffType.addChangeType(UpdateType.PARAMETER);

            }
            diffType.setBeforeAndAfter(oldP.get(0), newP.get(0), executable);
            diffTypes.add(diffType);
        } else {
            if (oldP.size() > 0) {
                for (int i = 0; i < oldP.size(); i++) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(instructionType);
                    if (bothChanged) {
                        diffType.addChangeType(DelType.EXECUTABLE_PARAMETER);
                    } else {
                        diffType.addChangeType(DelType.PARAMETER);
                    }
                    diffType.setBeforeAndAfter(oldP.get(i), null, executable);
                    diffTypes.add(diffType);
                }
            }
            if (newP.size() > 0) {
                for (int i = 0; i < newP.size(); i++) {
                    DiffType diffType = new DiffType(diff);
                    diffType.setInstruction(instructionType);
                    if (bothChanged) {
                        diffType.addChangeType(AddType.EXECUTABLE_PARAMETER);
                    } else {
                        diffType.addChangeType(AddType.PARAMETER);
                    }
                    diffType.setBeforeAndAfter(null, newP.get(i), executable);
                    diffTypes.add(diffType);
                }
            }
        }
        return diffTypes;
    }

    public static DiffType getDiffOfMaintainer(Maintainer oldMaintainer, Maintainer newMaintainer) {
        DiffType diffType = new DiffType(diff);
        diffType.setInstruction(Instructions.MAINAINER);
        if (oldMaintainer == null && newMaintainer != null) {
            diffType.addChangeType(AddType.MAINTAINER);
            diffType.setBeforeAndAfter(null, newMaintainer.maintainername);
        } else if (oldMaintainer != null && newMaintainer == null) {
            diffType.addChangeType(DelType.MAINTAINER);
            diffType.setBeforeAndAfter(oldMaintainer.maintainername, null);
        } else if (oldMaintainer != null && newMaintainer != null) {
            if (!oldMaintainer.maintainername.equals(newMaintainer.maintainername)) {
                diffType.addChangeType(UpdateType.MAINTAINER);
                diffType.setBeforeAndAfter(oldMaintainer.maintainername, newMaintainer.maintainername);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return diffType;
    }

    public static List<DiffType> getDiffOfFrom(From oldFrom, From newFrom) {
        List<DiffType> diffTypes = new ArrayList<>();
        if (oldFrom == null && newFrom != null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.FROM);
            diffType.addChangeType(AddType.FROM);
            diffType.setBeforeAndAfter(null, newFrom.fullName);
            diffTypes.add(diffType);
        } else if (oldFrom != null && newFrom == null) {
            DiffType diffType = new DiffType(diff);
            diffType.setInstruction(Instructions.FROM);
            diffType.addChangeType(DelType.FROM);
            diffType.setBeforeAndAfter(oldFrom.fullName, null);
            diffTypes.add(diffType);
        } else if (oldFrom != null && newFrom != null) {
            if (!oldFrom.imagename.equals(newFrom.imagename)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.FROM);
                diffType.addChangeType(UpdateType.IMAGE_NAME);
                diffType.setBeforeAndAfter(oldFrom.imagename, newFrom.imagename);
                diffTypes.add(diffType);
            }
            if (!oldFrom.imageVersionString.equals(newFrom.imageVersionString)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.FROM);
                diffType.addChangeType(UpdateType.IMAGE_VERSION_STRING);
                diffType.setBeforeAndAfter(oldFrom.imagename + ":" + oldFrom.imageVersionString,
                        newFrom.imagename + ":" + newFrom.imageVersionString);
                diffTypes.add(diffType);
            }
            if (oldFrom.imageVersionNumber != newFrom.imageVersionNumber) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.FROM);
                diffType.addChangeType(UpdateType.IMAGE_VERSION_NUMBER);
                diffType.setBeforeAndAfter(oldFrom.imagename + ":" + String.valueOf(oldFrom.imageVersionNumber),
                        String.valueOf(newFrom.imagename + ":" + newFrom.imageVersionNumber));
                diffTypes.add(diffType);
            }
            if (!oldFrom.imageVersionString.equals(newFrom.imageVersionString)) {
                DiffType diffType = new DiffType(diff);
                diffType.setInstruction(Instructions.FROM);
                diffType.addChangeType(UpdateType.IMAGE_VERSION_DIGEST);
                diffType.setBeforeAndAfter(oldFrom.imagename + ":" + oldFrom.imageVersionString,
                        newFrom.imagename + ":" + newFrom.imageVersionString);
                diffTypes.add(diffType);
            }
        }
        return diffTypes;
    }
}
