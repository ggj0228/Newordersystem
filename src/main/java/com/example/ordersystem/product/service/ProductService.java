package com.example.ordersystem.product.service;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.repository.MemeberRepository;
import com.example.ordersystem.product.domain.Product;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.dto.ProductResDto;
import com.example.ordersystem.product.dto.ProductSearchDto;
import com.example.ordersystem.product.dto.ProductUpdateDto;
import com.example.ordersystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemeberRepository memberRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Long createProduct(ProductCreateDto dto, MultipartFile productImage) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        Product product = productRepository.save(dto.toEntity(member));
        String fileName = "member-"+product.getId()+ "-productfileImage"+productImage.getOriginalFilename();
        // 저장 객체 구성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(productImage.getContentType()) // imag, jpeg, video/mp4
                .build();

        // 이미지를 업로드(byte형태로)
        // checked에러 나오는데 service 계층에서 롤백되야하니 try catch해야함
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes((productImage.getBytes())));
        } catch (Exception e) {
            // checked를 unchecked로 바꿔서  전체 내용이 rollback되도록 예외처리
            e.getMessage();
            e.printStackTrace();
            throw new IllegalArgumentException("이미지 업로드 실패");
        }

        // 이미지 삭제 시
//        s3Client.deleteObject(a -> a.bucket(버킷명).key(파일명))

        // 이미지 url 추출
        String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
        product.updateImageUrl(imgUrl);
        return product.getId();
    }

    public Long updateProduct(Long id, ProductUpdateDto dto) {
     String email = SecurityContextHolder.getContext().getAuthentication().getName();
     Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
     Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
     if(dto.getProductImage() != null) {
         String imgUrl = product.getProductImage();
         String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

         s3Client.deleteObject(a -> a.bucket(bucket).key(fileName));
        String newFileName = "member-"+product.getId()+ "-productfileImage"+dto.getProductImage().getOriginalFilename();
         PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                 .bucket(bucket)
                 .key(newFileName)
                 .contentType(dto.getProductImage().getContentType()) // imag, jpeg, video/mp4
                 .build();
         try {
             s3Client.putObject(putObjectRequest, RequestBody.fromBytes((dto.getProductImage().getBytes())));
         } catch (Exception e) {
             e.getMessage();
             e.printStackTrace();
             throw new IllegalArgumentException("이미지 업로드 실패");
         }

         String newImgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(newFileName)).toExternalForm();
         product.updateImageUrl(newImgUrl);
     }
     product.updateProduct(dto);
     Product newProduct = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
     return newProduct.getId();
    }


    public Page<ProductResDto> findAll(Pageable pageable, ProductSearchDto dto) {
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // Root : 엔티티의 속성을 접근하기 위한 객체
                // criteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();
                if (dto.getCategory() != null)
                    predicates.add(criteriaBuilder.equal(root.get("category"), dto.getCategory()));

                if (dto.getProductName() != null)
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + dto.getProductName() + "%"));

                Predicate[] predicateArr = predicates.toArray(Predicate[]::new);
                return criteriaBuilder.and(predicateArr);
            }
        };
        Page<Product> products = this.productRepository.findAll(specification, pageable);
        return products.map(ProductResDto::fromEntity);
    }

    public ProductResDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
        return ProductResDto.fromEntity(product);
    }
}
