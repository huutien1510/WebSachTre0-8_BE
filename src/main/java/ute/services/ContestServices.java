package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ute.dto.request.ContestRequest;
import ute.dto.response.ContestResponse;
import ute.entity.Book;
import ute.entity.Contest;
import ute.repository.ContestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContestServices {
    ContestRepository contestRepository;

    public Page<ContestResponse> getAllContest(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Contest> contests= contestRepository.findAll(pageable);
        return contests.map(contest -> new ContestResponse(
                        contest.getId(),
                        contest.getName(),
                        contest.getBanner(),
                        contest.getDescription(),
                        contest.getStart_date(),
                        contest.getEnd_date(),
                        contest.getMaxParticipants(),
                        contest.getCurrentParticipants()
                        ));
    }

    public ContestResponse getContestByID(Integer contestID){
        Contest contest = contestRepository.findById(contestID)
                .orElseThrow(()->new RuntimeException("Contest not found"));
        return new ContestResponse(
                contestID,
                contest.getName(),
                contest.getBanner(),
                contest.getDescription(),
                contest.getStart_date(),
                contest.getEnd_date(),
                contest.getMaxParticipants(),
                contest.getCurrentParticipants()
        );
    }

    public Contest addContest(ContestRequest body){
        Contest contest = Contest.builder()
                        .name(body.getName())
                        .banner(body.getBanner())
                        .description(body.getDescription())
                        .start_date(body.getStart_date())
                        .end_date(body.getEnd_date())
                        .maxParticipants(body.getMaxParticipants())
                        .currentParticipants(0)
                        .build();
        return contestRepository.save(contest);
    }

    public Contest updateContest(Integer contestID, ContestRequest body){
        Contest contest = contestRepository.findById(contestID)
                .orElseThrow(()->new RuntimeException("Contest not found"));
        contest.setName(body.getName());
        contest.setBanner(body.getBanner());
        contest.setDescription(body.getDescription());
        contest.setStart_date(body.getStart_date());
        contest.setEnd_date(body.getEnd_date());
        contest.setMaxParticipants(body.getMaxParticipants());

        return contestRepository.save(contest);
    }

    public void deleteContest(Integer contestID){
        contestRepository.deleteById(contestID);
    }
}
