package com.codecool.processwatch.os;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;

/**
 * A process source using the Java {@code ProcessHandle} API to retrieve information
 * about the current processes.
 */
public class OsProcessSource implements ProcessSource {
    //ProcessSource: interface ami egy streamet ad vissza processekről
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Process> getProcesses() {
        Stream<ProcessHandle> processStream = ProcessHandle.allProcesses();
        List<Process> processes = new ArrayList<>();
        long parentPId = 0;
        String userName = "";
        String name = "";
        String[] args = new String[0];

        for (ProcessHandle ph : processStream.collect(Collectors.toList())) {
            if (ph.parent().isPresent()) {
                parentPId = ph.parent().get().pid();
            } else {
                parentPId = 0;
            }
            if (ph.info().user().isPresent()) {
                userName = ph.info().user().get();
            } else {
                userName = "";
            }
            if (ph.info().command().isPresent()) {
                name = ph.info().command().get();
            } else {
                name = "";
            }
            if (ph.info().arguments().isPresent()) {
                args = ph.info().arguments().get();
            } else {
                args = new String[0];
            }

            processes.add(new Process(ph.pid(),
                    parentPId,
                    new User(userName),
                    name,
                    args));
        }

        return processes.stream();
    }
}
