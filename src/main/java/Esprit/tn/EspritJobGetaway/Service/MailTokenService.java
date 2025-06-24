package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.MailToken;
import Esprit.tn.EspritJobGetaway.Repository.MailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailTokenService {


        private final MailTokenRepository mailTokenRepository;

        public void saveConfirmationToken(MailToken token) {
            mailTokenRepository.save(token);
        }

        public Optional<MailToken> getToken(String token) {
            return mailTokenRepository.findByToken(token);
        }

        public int setConfirmedAt(String token) {
            return mailTokenRepository.updateConfirmedAt(
                    token, LocalDateTime.now());
        }

    }
