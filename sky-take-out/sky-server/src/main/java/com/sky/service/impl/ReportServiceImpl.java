package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.service.*;
import com.sky.utils.DateUtil;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.vo.order.OrderReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private WorkspaceService workspaceService;

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    @Override
    public TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = DateUtil.getDateList(begin, end);
        String dateListStr = StringUtils.join(dateList, ",");

        // 创建一个Double集合，用于存放从begin到end范围内每天的营业额
        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            Double turnover = orderService.countTurnoverByDate(date);
            turnoverList.add(turnover);
        }
        String turnoverListStr = StringUtils.join(turnoverList, ",");

        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();

    }


    // 用户统计：新增用户数和总用户数
    @Override
    public UserReportVO countUsers(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = DateUtil.getDateList(begin, end);

        List<Integer> newUserList=new ArrayList<>();
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            newUserList.add(userService.countNewUsersByDate(date));
            totalUserList.add(userService.countTotalUsersByDate(date));
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }


    // 订单统计：有效订单是指已完成订单
    @Override
    public OrderReportVO countOrders(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = DateUtil.getDateList(begin, end);

        List<Integer> validOrderList=new ArrayList<>();
        List<Integer> orderList=new ArrayList<>();
        Integer validOrderCount=0;
        Integer totalOrderCount=0;
        for (LocalDate date : dateList) {
            Integer daliyValidOrder = orderService.countValidOrderByDate(date);
            Integer daliyOrder = orderService.countOrderByDate(date);
            validOrderList.add(daliyValidOrder);
            orderList.add(daliyOrder);
            validOrderCount+=daliyValidOrder;
            totalOrderCount+=daliyOrder;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .orderCountList(StringUtils.join(orderList,","))
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate((double)validOrderCount/totalOrderCount)
                .build();
    }


    /**
     * 获取销量TOP10的商品
     * 必须是已完成的订单！
     */
    @Override
    public SalesTop10ReportVO getTop10Sales(LocalDate begin, LocalDate end) {

        List<GoodsSalesDTO> top10Sales = orderDetailService.getTop10Sales(begin, end);
        List<String> nameList=new ArrayList<>();
        List<Integer> numberList=new ArrayList<>();

        for (GoodsSalesDTO sale : top10Sales) {
            nameList.add(sale.getName());
            numberList.add(sale.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }


    // 运营数据导出为Excel报表
    @Override
    public void export(HttpServletResponse response) {

        // 1.查询数据库，获取近30天的运营数据（今天的运营数据不查，因为可能还会变化）
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO vo = workspaceService.getBusinessDataDuringPeriod(
                begin, end);

        // 2.通过POI将运营数据写入到Excel文件中
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template\\template.xlsx");
        try {
            // 基于模板Excel文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(is);
            // 获取Sheet1
            XSSFSheet sheet = excel.getSheetAt(0);

            // 填充日期区间：第2行第2个单元格
            sheet.getRow(1).getCell(1)
                    .setCellValue("日期："+begin+" 至 "+end);

            // 填充概览数据：第4~5行：3、5、7单元格
            XSSFRow row4 = sheet.getRow(3);
            XSSFRow row5 = sheet.getRow(4);
            row4.getCell(2).setCellValue(vo.getTurnover());
            row5.getCell(2).setCellValue(vo.getValidOrderCount());
            row4.getCell(4).setCellValue(vo.getOrderCompletionRate());
            row5.getCell(4).setCellValue(vo.getUnitPrice());
            row4.getCell(6).setCellValue(vo.getNewUsers());

            // 填充明细数据
            List<LocalDate> dateList = DateUtil.getDateList(begin, end);
            for (int i = 0; i < dateList.size(); i++) {
                LocalDate date = begin.plusDays(i);
                BusinessDataVO dailyVO= workspaceService.getBusinessDataByDate(date);
                XSSFRow row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(dailyVO.getTurnover());
                row.getCell(3).setCellValue(dailyVO.getValidOrderCount());
                row.getCell(4).setCellValue(dailyVO.getOrderCompletionRate());
                row.getCell(5).setCellValue(dailyVO.getUnitPrice());
                row.getCell(6).setCellValue(dailyVO.getNewUsers());
            }


            // 3.通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream os = response.getOutputStream();
            excel.write(os);

            // 4.关闭资源
            os.close();
            excel.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
