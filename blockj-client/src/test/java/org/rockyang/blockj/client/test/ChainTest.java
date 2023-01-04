package org.rockyang.blockj.client.test;

import org.junit.Test;
import org.rockyang.blockj.base.vo.JsonVo;

/**
 * @author yangjian
 */
public class ChainTest extends BaseTester
{

    @Test
    public void chainHead()
    {
        JsonVo<Long> res = serviceWrapper.chainHead();
        System.out.printf("Chain head: %d\n", res.getData());
    }
}
