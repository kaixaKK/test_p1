package com.taobao.config;

import com.taobao.entity.Order;
import com.taobao.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单超时取消服务
 */
@Service
public class OrderTimeoutService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 取消超时未支付的订单
     * @param orderId 订单ID
     * @return 是否成功取消
     */
    @Transactional
    public boolean cancelTimeoutOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            return false;
        }

        // 只取消待支付(PENDING=0)的订单
        if (order.getStatus() == 0) {
            order.setStatus(4); // CANCELLED 已取消
            return orderMapper.update(order) > 0;
        }

        // 如果订单已支付或已取消，移除超时任务
        return false;
    }
}
