filename = 'model1_val.log';
M = csvread(filename);
images_indices = unique(M(:, 1));

total_TP = zeros(size(images_indices, 1), 100);
total_condi_P = zeros(size(images_indices, 1), 100);
total_Pred_P = zeros(size(images_indices, 1), 100);

count=0;

for i=1:size(images_indices,1)
    count = count+1;
    
    current_index = images_indices(i);
    image_data = M(M(:,1)==current_index,:);
    probs = image_data(:,2);
    bboxs = image_data(:,3:6);
    
    [ TP_num, condi_P, Pred_P ] = evaluate_detector( bboxs, probs );
    total_TP(count,:) = TP_num;
    total_condi_P(count,:) = condi_P;
    total_Pred_P(count,:) = Pred_P;
end

% Summing the statistics over all faces images.
sTP = sum(total_TP);
sCP = sum(total_condi_P);
sPP = sum(total_Pred_P);

% Compute the Precision
% TP is the number of intersection betweem recognized faces and the
% actual faces

Precision = sTP./sPP;       % TP/(The number of recognized faces)
Recall = sTP./sCP;          % TP/(The number of actual faces)

% Ploting the Precision-Recall curve. Normally, the yaxis is the Precision
% and xaxis is the Recall.
figure
plot(Recall, Precision)
xlabel('Recall');
ylabel('Precision');


% Average Precision
% AP = Precision;
% AP(isnan(AP)) = 0;
% AP = mean(AP);
% disp(num2str(AP))

% Interpolated Average Precision
AP = VOCap(Recall', Precision');
disp(num2str(AP))