package cc.stbl.token.innerdisc.modules.scheduler;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class VrUserCountSchedulerTest extends AbstractTestCase {

    @Autowired
    private VrUserCountScheduler vrUserCountScheduler;

    @Test
    public void infiniteWar() {
        vrUserCountScheduler.infiniteWar();
    }
}