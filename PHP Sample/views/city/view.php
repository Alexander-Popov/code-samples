<?php

use yii\helpers\Html;
use yii\widgets\DetailView;
use app\Helper\View\CityViewHelper;

/* @var $this yii\web\View */

$this->title = $dto->name;
?>
<div class="city-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Обновить', ['update', 'id' =>  $dto->id], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Удалить', ['delete', 'id' =>  $dto->id], [
            'class' => 'btn btn-danger',
            'data' => [
                'confirm' => 'Вы уверены, что хотите удалить город?',
                'method' => 'post',
            ],
        ]) ?>
    </p>

    <?= DetailView::widget([
        'model' => $dto,
        'attributes' => [
            [
                'attribute' => 'name',
                'format' => 'raw',
                'label' => CityViewHelper::getAttributeLabel('name'),
            ],
            [
                'attribute' => 'country',
                'value' => CityViewHelper::getCountriesArray()[$dto->country],
                'label' => CityViewHelper::getAttributeLabel('country'),
            ],
            [
                'attribute' => 'extraCharge',
                'value' => $dto->extraCharge,
                'label' => CityViewHelper::getAttributeLabel('extraCharge'),
            ]
        ],
    ]) ?>

</div>
