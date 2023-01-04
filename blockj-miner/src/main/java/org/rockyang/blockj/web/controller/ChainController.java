package org.rockyang.blockj.web.controller;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.service.BlockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * chain api handler
 */
@RestController
@RequestMapping("/chain")
public class ChainController
{
    private final BlockService blockService;

    public ChainController(BlockService blockService)
    {
        this.blockService = blockService;
    }

    @GetMapping("/head")
    public JsonVo head()
    {
        long head = blockService.chainHead();
        if (head < 0) {
            return new JsonVo(JsonVo.FAIL, "Invalid chain head found");
        }

        return new JsonVo<>(JsonVo.SUCCESS, head);
    }

    @GetMapping("/block/{height}")
    public JsonVo getBlock(@PathVariable Long height)
    {
        if (height <= 0) {
            return new JsonVo(JsonVo.FAIL, "Invalid block height");
        }
        Block block = blockService.getBlockByHeight(height);

        if (block == null) {
            return new JsonVo(JsonVo.FAIL, "Block not found");
        }

        return new JsonVo<>(JsonVo.SUCCESS, block);
    }
}
