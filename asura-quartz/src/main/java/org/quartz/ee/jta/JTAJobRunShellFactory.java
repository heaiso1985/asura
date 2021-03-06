
/* 
 * Copyright 2001-2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package org.quartz.ee.jta;

import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.core.JobRunShell;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.SchedulingContext;

/**
 * <p>
 * Responsible for creating the instances of <code>{@link org.quartz.ee.jta.JTAJobRunShell}</code>
 * to be used within the <class>{@link org.quartz.core.QuartzScheduler}
 * </code> instance.
 * </p>
 * 
 * <p>
 * This implementation does not re-use any objects, it simply makes a new
 * JTAJobRunShell each time <code>borrowJobRunShell()</code> is called.
 * </p>
 * 
 * @author James House
 */
public class JTAJobRunShellFactory implements JobRunShellFactory {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private Scheduler scheduler;

    private SchedulingContext schedCtxt;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public JTAJobRunShellFactory() {
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Initialize the factory, providing a handle to the <code>Scheduler</code>
     * that should be made available within the <code>JobRunShell</code> and
     * the <code>JobExecutionContext</code> s within it, and a handle to the
     * <code>SchedulingContext</code> that the shell will use in its own
     * operations with the <code>JobStore</code>.
     * </p>
     */
    public void initialize(Scheduler scheduler, SchedulingContext schedCtxt)
        throws SchedulerConfigException {
        this.scheduler = scheduler;
        this.schedCtxt = schedCtxt;
    }

    /**
     * <p>
     * Called by the <class>{@link org.quartz.core.QuartzSchedulerThread}
     * </code> to obtain instances of <code>
     * {@link org.quartz.core.JobRunShell}</code>.
     * </p>
     */
    public JobRunShell borrowJobRunShell() {
        return new JTAJobRunShell(this, scheduler, schedCtxt);
    }

    /**
     * <p>
     * Called by the <class>{@link org.quartz.core.QuartzSchedulerThread}
     * </code> to return instances of <code>
     * {@link org.quartz.core.JobRunShell}</code>.
     * </p>
     */
    public void returnJobRunShell(JobRunShell jobRunShell) {
        jobRunShell.passivate();
    }

}
