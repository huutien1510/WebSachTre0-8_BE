package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.request.ContestantSubmitRequest;
import ute.dto.response.ContestantResponse;
import ute.entity.Account;
import ute.entity.Contest;
import ute.entity.Contestant;
import ute.repository.AccountRepository;
import ute.repository.ContestRepository;
import ute.repository.ContestantRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContestantServices {
    ContestantRepository contestantRepository;
    AccountRepository accountRepository;
    ContestRepository contestRepository;

    public ContestantResponse isRegister(Integer accountID, Integer contestID){
        Contestant contestant = contestantRepository.isRegister(accountID,contestID);
        if (contestant==null) return null;
        return new ContestantResponse(
                contestant.getId(),
                contestant.getContest().getId(),
                contestant.getAccount().getId(),
                contestant.getAccount().getName(),
                contestant.getSubmissionName(),
                contestant.getSubmission(),
                contestant.getSubmit_time(),
                contestant.getScore());
    }

    public List<ContestantResponse> getContestantByContest(Integer contestID){
        List<Contestant> contestants = contestantRepository.getContestantByContest(contestID);
        return contestants.stream().map(contestant -> new ContestantResponse(
                contestant.getId(),
                contestant.getContest().getId(),
                contestant.getAccount().getId(),
                contestant.getAccount().getName(),
                contestant.getSubmissionName(),
                contestant.getSubmission(),
                contestant.getSubmit_time(),
                contestant.getScore()
        )).collect(Collectors.toList());
    }

    public Contestant register(Integer accountID, Integer contestID){
        Account account = accountRepository.findById(accountID)
                .orElseThrow(()->new RuntimeException("Account not found"));
        Contest contest = contestRepository.findById(contestID)
                .orElseThrow(()->new RuntimeException("Contest not found"));

        contest.setCurrentParticipants(contest.getCurrentParticipants()+1);
        contestRepository.save(contest);

        Contestant contestant = Contestant.builder()
                .account(account)
                .contest(contest)
                .build();

        return contestantRepository.save(contestant);
    }

    public Contestant submitFile(ContestantSubmitRequest body){
        Contestant contestant = contestantRepository.findById(body.getContestantID())
                .orElseThrow(()->new RuntimeException("User didn't registered"));
        contestant.setSubmissionName(body.getSubmissionName());
        contestant.setSubmission(body.getSubmission());
        contestant.setSubmit_time(body.getSubmit_time());

        return contestantRepository.save(contestant);
    }

    public Contestant scoreContestant(Integer contestantID, Map<String,Integer> body){
        Contestant contestant = contestantRepository.findById(contestantID)
                .orElseThrow(()->new RuntimeException("User didn't registered"));

        contestant.setScore(body.get("score"));

        return contestantRepository.save(contestant);
    }
}
