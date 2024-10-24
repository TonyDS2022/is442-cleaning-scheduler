package com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler;

import com.google.api.gax.paging.Page;
import com.google.cloud.Policy;
import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NoOpsStorage implements Storage {

    @Override
    public Bucket create(BucketInfo bucketInfo, BucketTargetOption... bucketTargetOptions) {
        System.out.println("NoOpsStorage: create bucket ignored!");
        return null;
    }

    @Override
    public Blob create(BlobInfo blobInfo, BlobTargetOption... blobTargetOptions) {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob create(BlobInfo blobInfo, byte[] bytes, BlobTargetOption... blobTargetOptions) {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob create(BlobInfo blobInfo, byte[] bytes, int i, int i1, BlobTargetOption... blobTargetOptions) {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob create(BlobInfo blobInfo, InputStream inputStream, BlobWriteOption... blobWriteOptions) {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob createFrom(BlobInfo blobInfo, Path path, BlobWriteOption... blobWriteOptions) throws IOException {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob createFrom(BlobInfo blobInfo, Path path, int i, BlobWriteOption... blobWriteOptions) throws IOException {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob createFrom(BlobInfo blobInfo, InputStream inputStream, BlobWriteOption... blobWriteOptions) throws IOException {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Blob createFrom(BlobInfo blobInfo, InputStream inputStream, int i, BlobWriteOption... blobWriteOptions) throws IOException {
        System.out.println("NoOpsStorage: create blob ignored!");
        return null;
    }

    @Override
    public Bucket get(String s, BucketGetOption... bucketGetOptions) {
        System.out.println("NoOpsStorage: get bucket ignored!");
        return null;
    }

    @Override
    public Bucket lockRetentionPolicy(BucketInfo bucketInfo, BucketTargetOption... bucketTargetOptions) {
        System.out.println("NoOpsStorage: lockRetentionPolicy ignored!");
        return null;
    }

    @Override
    public Blob get(String s, String s1, BlobGetOption... blobGetOptions) {
        System.out.println("NoOpsStorage: get blob ignored!");
        return null;
    }

    @Override
    public Blob get(BlobId blobId, BlobGetOption... blobGetOptions) {
        System.out.println("NoOpsStorage: get blob ignored!");
        return null;
    }

    @Override
    public Blob get(BlobId blobId) {
        System.out.println("NoOpsStorage: get blob ignored!");
        return null;
    }

    @Override
    public Blob restore(BlobId blobId, BlobRestoreOption... blobRestoreOptions) {
        System.out.println("NoOpsStorage: restore blob ignored!");
        return null;
    }

    @Override
    public Page<Bucket> list(BucketListOption... bucketListOptions) {
        System.out.println("NoOpsStorage: list bucket ignored!");
        return null;
    }

    @Override
    public Page<Blob> list(String s, BlobListOption... blobListOptions) {
        System.out.println("NoOpsStorage: list blob ignored!");
        return null;
    }

    @Override
    public Bucket update(BucketInfo bucketInfo, BucketTargetOption... bucketTargetOptions) {
        System.out.println("NoOpsStorage: update bucket ignored!");
        return null;
    }

    @Override
    public Blob update(BlobInfo blobInfo, BlobTargetOption... blobTargetOptions) {
        System.out.println("NoOpsStorage: update blob ignored!");
        return null;
    }

    @Override
    public Blob update(BlobInfo blobInfo) {
        System.out.println("NoOpsStorage: update blob ignored!");
        return null;
    }

    @Override
    public boolean delete(String s, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: delete bucket ignored!");
        return false;
    }

    @Override
    public boolean delete(String s, String s1, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: delete blob ignored!");
        return false;
    }

    @Override
    public boolean delete(BlobId blobId, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: delete blob ignored!");
        return false;
    }

    @Override
    public boolean delete(BlobId blobId) {
        System.out.println("NoOpsStorage: delete blob ignored!");
        return false;
    }

    @Override
    public Blob compose(ComposeRequest composeRequest) {
        System.out.println("NoOpsStorage: compose ignored!");
        return null;
    }

    @Override
    public CopyWriter copy(CopyRequest copyRequest) {
        System.out.println("NoOpsStorage: copy ignored!");
        return null;
    }

    @Override
    public byte[] readAllBytes(String s, String s1, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: readAllBytes ignored!");
        return new byte[0];
    }

    @Override
    public byte[] readAllBytes(BlobId blobId, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: readAllBytes ignored!");
        return new byte[0];
    }

    @Override
    public StorageBatch batch() {
        System.out.println("NoOpsStorage: batch ignored!");
        return null;
    }

    @Override
    public ReadChannel reader(String s, String s1, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: reader ignored!");
        return null;
    }

    @Override
    public ReadChannel reader(BlobId blobId, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: reader ignored!");
        return null;
    }

    @Override
    public void downloadTo(BlobId blobId, Path path, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: downloadTo ignored!");
    }

    @Override
    public void downloadTo(BlobId blobId, OutputStream outputStream, BlobSourceOption... blobSourceOptions) {
        System.out.println("NoOpsStorage: downloadTo ignored!");
    }

    @Override
    public WriteChannel writer(BlobInfo blobInfo, BlobWriteOption... blobWriteOptions) {
        System.out.println("NoOpsStorage: writer ignored!");
        return null;
    }

    @Override
    public WriteChannel writer(URL url) {
        System.out.println("NoOpsStorage: writer ignored!");
        return null;
    }

    @Override
    public URL signUrl(BlobInfo blobInfo, long l, TimeUnit timeUnit, SignUrlOption... signUrlOptions) {
        System.out.println("NoOpsStorage: signUrl ignored!");
        return null;
    }

    @Override
    public PostPolicyV4 generateSignedPostPolicyV4(BlobInfo blobInfo, long l, TimeUnit timeUnit, PostPolicyV4.PostFieldsV4 postFieldsV4, PostPolicyV4.PostConditionsV4 postConditionsV4, PostPolicyV4Option... postPolicyV4Options) {
        System.out.println("NoOpsStorage: generateSignedPostPolicyV4 ignored!");
        return null;
    }

    @Override
    public PostPolicyV4 generateSignedPostPolicyV4(BlobInfo blobInfo, long l, TimeUnit timeUnit, PostPolicyV4.PostFieldsV4 postFieldsV4, PostPolicyV4Option... postPolicyV4Options) {
        System.out.println("NoOpsStorage: generateSignedPostPolicyV4 ignored!");
        return null;
    }

    @Override
    public PostPolicyV4 generateSignedPostPolicyV4(BlobInfo blobInfo, long l, TimeUnit timeUnit, PostPolicyV4.PostConditionsV4 postConditionsV4, PostPolicyV4Option... postPolicyV4Options) {
        System.out.println("NoOpsStorage: generateSignedPostPolicyV4 ignored!");
        return null;
    }

    @Override
    public PostPolicyV4 generateSignedPostPolicyV4(BlobInfo blobInfo, long l, TimeUnit timeUnit, PostPolicyV4Option... postPolicyV4Options) {
        System.out.println("NoOpsStorage: generateSignedPostPolicyV4 ignored!");
        return null;
    }

    @Override
    public List<Blob> get(BlobId... blobIds) {
        System.out.println("NoOpsStorage: get ignored!");
        return List.of();
    }

    @Override
    public List<Blob> get(Iterable<BlobId> iterable) {
        System.out.println("NoOpsStorage: get ignored!");
        return List.of();
    }

    @Override
    public List<Blob> update(BlobInfo... blobInfos) {
        System.out.println("NoOpsStorage: update ignored!");
        return List.of();
    }

    @Override
    public List<Blob> update(Iterable<BlobInfo> iterable) {
        System.out.println("NoOpsStorage: update ignored!");
        return List.of();
    }

    @Override
    public List<Boolean> delete(BlobId... blobIds) {
        System.out.println("NoOpsStorage: delete ignored!");
        return List.of();
    }

    @Override
    public List<Boolean> delete(Iterable<BlobId> iterable) {
        System.out.println("NoOpsStorage: delete ignored!");
        return List.of();
    }

    @Override
    public Acl getAcl(String s, Acl.Entity entity, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: getAcl ignored!");
        return null;
    }

    @Override
    public Acl getAcl(String s, Acl.Entity entity) {
        System.out.println("NoOpsStorage: getAcl ignored!");
        return null;
    }

    @Override
    public boolean deleteAcl(String s, Acl.Entity entity, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: deleteAcl ignored!");
        return false;
    }

    @Override
    public boolean deleteAcl(String s, Acl.Entity entity) {
        System.out.println("NoOpsStorage: deleteAcl ignored!");
        return false;
    }

    @Override
    public Acl createAcl(String s, Acl acl, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: createAcl ignored!");
        return null;
    }

    @Override
    public Acl createAcl(String s, Acl acl) {
        System.out.println("NoOpsStorage: createAcl ignored!");
        return null;
    }

    @Override
    public Acl updateAcl(String s, Acl acl, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: updateAcl ignored!");
        return null;
    }

    @Override
    public Acl updateAcl(String s, Acl acl) {
        System.out.println("NoOpsStorage: updateAcl ignored!");
        return null;
    }

    @Override
    public List<Acl> listAcls(String s, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: listAcls ignored!");
        return List.of();
    }

    @Override
    public List<Acl> listAcls(String s) {
        System.out.println("NoOpsStorage: listAcls ignored!");
        return List.of();
    }

    @Override
    public Acl getDefaultAcl(String s, Acl.Entity entity) {
        System.out.println("NoOpsStorage: getDefaultAcl ignored!");
        return null;
    }

    @Override
    public boolean deleteDefaultAcl(String s, Acl.Entity entity) {
        System.out.println("NoOpsStorage: deleteDefaultAcl ignored!");
        return false;
    }

    @Override
    public Acl createDefaultAcl(String s, Acl acl) {
        System.out.println("NoOpsStorage: createDefaultAcl ignored!");
        return null;
    }

    @Override
    public Acl updateDefaultAcl(String s, Acl acl) {
        System.out.println("NoOpsStorage: updateDefaultAcl ignored!");
        return null;
    }

    @Override
    public List<Acl> listDefaultAcls(String s) {
        System.out.println("NoOpsStorage: listDefaultAcls ignored!");
        return List.of();
    }

    @Override
    public Acl getAcl(BlobId blobId, Acl.Entity entity) {
        System.out.println("NoOpsStorage: getAcl ignored!");
        return null;
    }

    @Override
    public boolean deleteAcl(BlobId blobId, Acl.Entity entity) {
        System.out.println("NoOpsStorage: deleteAcl ignored!");
        return false;
    }

    @Override
    public Acl createAcl(BlobId blobId, Acl acl) {
        System.out.println("NoOpsStorage: createAcl ignored!");
        return null;
    }

    @Override
    public Acl updateAcl(BlobId blobId, Acl acl) {
        System.out.println("NoOpsStorage: updateAcl ignored!");
        return null;
    }

    @Override
    public List<Acl> listAcls(BlobId blobId) {
        System.out.println("NoOpsStorage: listAcls ignored!");
        return List.of();
    }

    @Override
    public HmacKey createHmacKey(ServiceAccount serviceAccount, CreateHmacKeyOption... createHmacKeyOptions) {
        System.out.println("NoOpsStorage: createHmacKey ignored!");
        return null;
    }

    @Override
    public Page<HmacKey.HmacKeyMetadata> listHmacKeys(ListHmacKeysOption... listHmacKeysOptions) {
        System.out.println("NoOpsStorage: listHmacKeys ignored!");
        return null;
    }

    @Override
    public HmacKey.HmacKeyMetadata getHmacKey(String s, GetHmacKeyOption... getHmacKeyOptions) {
        System.out.println("NoOpsStorage: getHmacKey ignored!");
        return null;
    }

    @Override
    public void deleteHmacKey(HmacKey.HmacKeyMetadata hmacKeyMetadata, DeleteHmacKeyOption... deleteHmacKeyOptions) {
        System.out.println("NoOpsStorage: deleteHmacKey ignored!");
    }

    @Override
    public HmacKey.HmacKeyMetadata updateHmacKeyState(HmacKey.HmacKeyMetadata hmacKeyMetadata, HmacKey.HmacKeyState hmacKeyState, UpdateHmacKeyOption... updateHmacKeyOptions) {
        System.out.println("NoOpsStorage: updateHmacKeyState ignored!");
        return null;
    }

    @Override
    public Policy getIamPolicy(String s, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: getIamPolicy ignored!");
        return null;
    }

    @Override
    public Policy setIamPolicy(String s, Policy policy, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: setIamPolicy ignored!");
        return null;
    }

    @Override
    public List<Boolean> testIamPermissions(String s, List<String> list, BucketSourceOption... bucketSourceOptions) {
        System.out.println("NoOpsStorage: testIamPermissions ignored!");
        return List.of();
    }

    @Override
    public ServiceAccount getServiceAccount(String s) {
        System.out.println("NoOpsStorage: getServiceAccount ignored!");
        return null;
    }

    @Override
    public Notification createNotification(String s, NotificationInfo notificationInfo) {
        System.out.println("NoOpsStorage: createNotification ignored!");
        return null;
    }

    @Override
    public Notification getNotification(String s, String s1) {
        System.out.println("NoOpsStorage: getNotification ignored!");
        return null;
    }

    @Override
    public List<Notification> listNotifications(String s) {
        System.out.println("NoOpsStorage: listNotifications ignored!");
        return List.of();
    }

    @Override
    public boolean deleteNotification(String s, String s1) {
        System.out.println("NoOpsStorage: deleteNotification ignored!");
        return false;
    }

    @Override
    public StorageOptions getOptions() {
        System.out.println("NoOpsStorage: getOptions ignored!");
        return null;
    }
}
