package kr.co.company.as.application;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import kr.co.company.as.application.dto.AsRequest;
import kr.co.company.as.domain.As;
import kr.co.company.as.domain.CommonInfoType;
import kr.co.company.as.mappers.AsMapper;
import kr.co.company.common.ResponseCode;
import kr.co.company.common.domain.BaseResponse;
import kr.co.company.common.domain.PageNavigation;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class AsService {
    
    private final AsMapper asMapper;
    
    public AsService(AsMapper asMapper) {
        this.asMapper = asMapper;
    }
    
    public Map<String, Object> findAsCount(AsRequest asRequest) {
        Map<String, Object> response = new HashMap<>();
        List<CommonInfoType> list = asMapper.findAsCount(
            String.valueOf(asRequest.getPtnCompSeq()));
        response.put("data", list);
        return response;
    }
    
    public Map<String, Object> findAsTop5(AsRequest asRequest) {
        Map<String, Object> response = new HashMap<>();
        List<As> list = asMapper.findAsTop5(String.valueOf(asRequest.getPtnCompSeq()));
        response.put("data", list);
        return response;
    }
    
    public Map<String, Object> findAsList(AsRequest asRequest) {
        Map<String, Object> response = new HashMap<>();
        PageHelper.startPage(asRequest.getPage(), 10);
        Page<As> list = (Page<As>) asMapper.findAsList(asRequest);
        response.put("page", new PageNavigation(list));
        response.put("data", list);
        
        return response;
    }
    
    public void findAsListExcel(AsRequest asRequest, HttpServletResponse response)
        throws IOException {
        OutputStream out = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        XSSFDataFormat format = workbook.createDataFormat();
        // 테이블 헤더용 스타일
        XSSFCellStyle headStyle = workbook.createCellStyle();
        
        // 가는 경계선을 가집니다.
        headStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        
        // 배경색은 노란색입니다.
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setFont(font);
        
        // 테이블 헤더용 값 스타일
        XSSFCellStyle valueheadStyle = workbook.createCellStyle();
        valueheadStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        valueheadStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        valueheadStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        valueheadStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        valueheadStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        valueheadStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 데이터는 가운데 정렬합니다.
        valueheadStyle.setAlignment(CellStyle.ALIGN_LEFT);
        valueheadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        valueheadStyle.setFont(font);
        
        XSSFCellStyle mumheadStyle = workbook.createCellStyle();
        
        // 가는 경계선을 가집니다.
        mumheadStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        mumheadStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        mumheadStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        mumheadStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        
        // 배경색은 노란색입니다.
        mumheadStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        mumheadStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        // 데이터는 가운데 정렬합니다.
        mumheadStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        mumheadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        mumheadStyle.setFont(font);
        mumheadStyle.setDataFormat(format.getFormat("#,##0"));
        
        // 데이터용 경계 스타일 테두리만 지정
        XSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        
        XSSFCellStyle numBodyStyle = workbook.createCellStyle();
        numBodyStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        numBodyStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        numBodyStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        numBodyStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        numBodyStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        numBodyStyle.setDataFormat(format.getFormat("#,##0"));
        
        // 시트생성
        XSSFSheet sheet = workbook.createSheet("보증기간 관리");
        int rowNo = 0;
        
        XSSFRow headerRow = sheet.createRow(rowNo++);
        headerRow.setHeight((short) 512);
        String[] pageheaderKey = {"페이지명", "단말기 이력관리"};
        
        XSSFCell headerCell = null;
        for (int i = 0; i < pageheaderKey.length; i++) {
            
            headerCell = headerRow.createCell(i);
            
            if (i == 0) {
                headerCell.setCellStyle(headStyle);
            } else {
                headerCell.setCellStyle(valueheadStyle);
            }
            
            headerCell.setCellValue(pageheaderKey[i]);
        }
        sheet.addMergedRegion(
            new CellRangeAddress(rowNo - 1, rowNo - 1, 1, pageheaderKey.length - 1));
        
        headerRow = sheet.createRow(rowNo++);
        headerRow.setHeight((short) 512);
        headerRow = sheet.createRow(rowNo++);
        headerRow.setHeight((short) 212);
        
        String[] headerKey = {"등록일자", "처리일자", "보증만료일자", "CID", "발송처", "협력사",
            "단말기모델", "TID", "Serial", "SAM유무", "불량내역",
            "수리내역", "메인REV", "서브REV", "ICREV", "비고"
        };
        
        headerRow.setHeight((short) 512);
        int length = headerKey.length;
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 3024);
            headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(headStyle);
            headerCell.setCellValue(headerKey[i]);
        }
        // 데이터 부분 생성
        XSSFRow bodyRow = null;
        XSSFCell bodyCell = null;
        int colno = 0;
        List<As> list = asMapper.findAsList(asRequest);
        for (int i = 0; i < list.size(); i++) {
            //vm = list.get(i);
            
            bodyRow = sheet.createRow(rowNo++);
            colno = 0;
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getRepairDate());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getRepairExpireDate());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getCid());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getCompName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getPtnCompName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getTidModelName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getTerminalId());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getSerialNo());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            //2024.04.25 twkim 요청사항: Flag 에 따라 o, x (소문자)로 표시
            bodyCell.setCellValue(list.get(i).getSamFlag() == 'Y' ? "o" : "x");
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getTrbName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getAtName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getMbRvName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getSbRvName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getIcRvName());
            
            bodyCell = bodyRow.createCell(colno++);
            bodyCell.setCellStyle(bodyStyle);
            bodyCell.setCellValue(list.get(i).getPtnComment());
        }
        
        response.reset();
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode("단말기AS 접수내역.xlsx", "UTF-8"));
        response.setContentType("application/vnd.ms-excel");
        out = new BufferedOutputStream(response.getOutputStream());
        
        workbook.write(out);
        out.flush();
    }
    
    public As findAsByAsNo(String asNo) {
        return asMapper.findAsByAsNo(asNo);
    }
    
    public BaseResponse patchAs(String asNo, As as) {
        if (asMapper.patchAs(asNo, as)) {
            return new BaseResponse(ResponseCode.SUCCESS_INSERT);
        } else {
            return new BaseResponse(ResponseCode.ERROR_INSERT);
        }
    }
}
