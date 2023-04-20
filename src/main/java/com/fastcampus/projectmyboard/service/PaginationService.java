package com.fastcampus.projectmyboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {//현재페이지와 마지막페이지로 페이징 구현
        int startNumber = Math.max(currentPageNumber-(currentBarLength()/2),0); //현재 페이지 - (페이징바 길이 / 2) 를 하면 음수가 반환 되는 경우가 있으니 Math.max함수를 사용하여 이를 해결함
        int endNumber = Math.min(startNumber+BAR_LENGTH,totalPages);
        return IntStream.range(startNumber,endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
